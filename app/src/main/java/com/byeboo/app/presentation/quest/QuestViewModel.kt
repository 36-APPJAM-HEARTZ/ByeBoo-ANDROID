package com.byeboo.app.presentation.quest

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.byeboo.app.core.designsystem.type.CustomSnackBarType
import com.byeboo.app.core.model.quest.QuestType
import com.byeboo.app.core.util.DateUtil.getFormattedDate
import com.byeboo.app.core.util.MixpanelUtil
import com.byeboo.app.core.util.TimeUtil
import com.byeboo.app.domain.model.home.HomeStatus
import com.byeboo.app.domain.repository.auth.UserRepository
import com.byeboo.app.domain.repository.quest.QuestCommonRepository
import com.byeboo.app.domain.repository.quest.QuestStateRepository
import com.byeboo.app.domain.usecase.quest.QuestUseCase
import com.byeboo.app.presentation.quest.model.CommonAnswerModel
import com.byeboo.app.presentation.quest.model.Quest
import com.byeboo.app.presentation.quest.model.QuestSideEffect
import com.byeboo.app.presentation.quest.model.QuestState
import com.byeboo.app.presentation.quest.model.QuestTab
import com.byeboo.app.presentation.quest.util.QuestCountdownTimer
import com.byeboo.app.presentation.quest.util.QuestUiModelMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.concurrent.CancellationException
import javax.inject.Inject

@HiltViewModel
class QuestViewModel
@Inject
constructor(
    private val savedStateHandle: SavedStateHandle,
    private val questUseCase: QuestUseCase,
    private val questStateRepository: QuestStateRepository,
    private val userRepository: UserRepository,
    private val commonQuestRepository: QuestCommonRepository,
    private val mixpanelUtil: MixpanelUtil,
    private val mapper: QuestUiModelMapper,
) : ViewModel() {
    private val _uiState =
        MutableStateFlow(
            QuestUiState(
                selectedTab =
                    savedStateHandle
                        .get<String>("selectedTab")
                        ?.let { QuestTab.valueOf(it) }
                        ?: QuestTab.MY_JOURNEY,
            ),
        )
    val uiState: StateFlow<QuestUiState> = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<QuestSideEffect>()
    val sideEffect: SharedFlow<QuestSideEffect> = _sideEffect.asSharedFlow()

    private var fetchJob: Job? = null
    private var paginationJob: Job? = null
    private var countdownJob: Job? = null

    init {
        viewModelScope.launch {
            userRepository.getNickname().collect { nickname ->
                _uiState.update { it.copy(userName = nickname) }
            }
        }
        loadQuestStatus()
        refetchCommonQuests(uiState.value.commonJourneyState.selectedDate)
        observeAnswerSubmitted()
        observeRefreshEvent()
        observeDeeplinkQuestId()
    }

    fun onTabClicked(tab: QuestTab) {
        savedStateHandle["selectedTab"] = tab.name
        _uiState.update { it.copy(selectedTab = tab) }

        if (tab == QuestTab.COMMON_JOURNEY) {
            mixpanelUtil.trackEvent("common_journey_pageview")
        }
    }

    fun onDateChange(newDate: LocalDate) {
        if (!TimeUtil.isValidDateRange(newDate)) return

        paginationJob?.cancel()

        _uiState.update { state ->
            state.copy(
                commonJourneyState =
                    CommonJourneyState(
                        selectedDate = newDate,
                        isLoading = true,
                    ),
            )
        }
        refetchCommonQuests(newDate)
    }

    private fun refetchCommonQuests(date: LocalDate) {
        fetchJob?.cancel()
        fetchJob =
            viewModelScope.launch {
                delay(100)
                fetchCommonQuests(date)
            }
    }

    private suspend fun fetchCommonQuests(date: LocalDate) {
        runCatching {
            commonQuestRepository
                .getCommonQuests(
                    date = date.toString(),
                    cursor = null,
                    limit = 20,
                ).getOrThrow()
        }.map { domainModel ->
            val uiAnswers =
                domainModel.answers
                    .map { answer ->
                        CommonAnswerModel(
                            heartCount = answer.heartCount,
                            commentCount = answer.commentCount,
                            isLiked = answer.isLiked,
                            answerId = answer.answerId,
                            writerId = answer.writerId,
                            writer = answer.writer,
                            profileIconRes = mapper.mapToIconRes(answer.profileIcon),
                            displayTime = mapper.formatWrittenTime(answer.writtenAt),
                            content = answer.content,
                        )
                    }.toImmutableList()

            uiState.value.commonJourneyState.copy(
                isLoading = false,
                isPaginationLoading = false,
                question = domainModel.question,
                questId = domainModel.questId,
                answerCount = domainModel.answerCount.toInt(),
                answers = uiAnswers,
                isMyAnswerDone = domainModel.isAnswered,
                selectedDate = date,
                hasNext = domainModel.hasNext,
                nextCursor = domainModel.nextCursor,
            )
        }.onSuccess { updatedCommonState ->
            _uiState.update { state ->
                if (state.commonJourneyState.selectedDate == date) {
                    state.copy(commonJourneyState = updatedCommonState)
                } else {
                    state
                }
            }
        }.onFailure { t ->
            if (t is CancellationException) throw t
            _uiState.update { state ->
                state.copy(
                    commonJourneyState =
                        state.commonJourneyState.copy(
                            isLoading = false,
                            isPaginationLoading = false,
                        ),
                )
            }
            _sideEffect.emit(QuestSideEffect.ShowSnackBar(CustomSnackBarType.ALERT))
        }
    }

    fun loadNextPage() {
        val currentState = uiState.value.commonJourneyState
        val requestDate = currentState.selectedDate

        if (currentState.isLoading ||
            currentState.isPaginationLoading ||
            !currentState.hasNext ||
            currentState.nextCursor == null
        ) {
            return
        }

        paginationJob?.cancel()
        paginationJob =
            viewModelScope.launch {
                _uiState.update {
                    it.copy(
                        commonJourneyState = it.commonJourneyState.copy(
                            isPaginationLoading = true
                        )
                    )
                }

                runCatching {
                    commonQuestRepository
                        .getCommonQuests(
                            date = requestDate.toString(),
                            cursor = currentState.nextCursor,
                            limit = 20,
                        ).getOrThrow()
                }.onSuccess { domainModel ->
                    val moreAnswers =
                        domainModel.answers.map { answer ->
                            CommonAnswerModel(
                                heartCount = answer.heartCount,
                                commentCount = answer.commentCount,
                                isLiked = answer.isLiked,
                                answerId = answer.answerId,
                                writer = answer.writer,
                                writerId = answer.writerId,
                                profileIconRes = mapper.mapToIconRes(answer.profileIcon),
                                displayTime = mapper.formatWrittenTime(answer.writtenAt),
                                content = answer.content,
                            )
                        }

                    _uiState.update { state ->
                        if (state.commonJourneyState.selectedDate != requestDate) {
                            return@update state.copy(
                                commonJourneyState = state.commonJourneyState.copy(
                                    isPaginationLoading = false
                                ),
                            )
                        }

                        state.copy(
                            commonJourneyState =
                                state.commonJourneyState.copy(
                                    answers = (state.commonJourneyState.answers + moreAnswers).toImmutableList(),
                                    hasNext = domainModel.hasNext,
                                    nextCursor = domainModel.nextCursor,
                                    isPaginationLoading = false,
                                ),
                        )
                    }
                }.onFailure { t ->
                    if (t is CancellationException) throw t
                    _uiState.update {
                        it.copy(
                            commonJourneyState = it.commonJourneyState.copy(
                                isPaginationLoading = false
                            )
                        )
                    }
                    _sideEffect.emit(QuestSideEffect.ShowSnackBar(CustomSnackBarType.ALERT))
                }
            }
    }

    private fun loadQuestStatus() {
        viewModelScope.launch {
            var status = HomeStatus.INITIAL_START

            questStateRepository
                .getQuestCount()
                .onSuccess { model ->
                    status = HomeStatus.from(model.userCurrentStatus)

                    _uiState.update {
                        it.copy(
                            status = status,
                            isStatusLoading = false,
                        )
                    }
                }.onFailure {
                    _sideEffect.emit(QuestSideEffect.ShowSnackBar(CustomSnackBarType.ALERT))
                }

            if (status != HomeStatus.JOURNEY_COMPLETE) {
                loadQuests()
            }
        }
    }

    private fun loadQuests() {
        viewModelScope.launch {
            runCatching { questUseCase() }
                .mapCatching { data -> mapper.mapToPresentationModel(data) }
                .onSuccess { output ->
                    mixpanelUtil.trackEvent(
                        "quest_pageview",
                        mapOf(
                            "journey_type" to output.journeyTitle,
                            "is_first_pageview" to false,
                        ),
                    )
                    _uiState.update {
                        it.copy(
                            myJourneyState =
                                it.myJourneyState.copy(
                                    questGroups = output.questGroups,
                                    currentStepIndex = output.activeStepIndex,
                                    progressPeriod = output.progressPeriod,
                                    journeyTitle = output.journeyTitle,
                                    completedQuestCount = output.questCompletedCount,
                                ),
                            error = null,
                        )
                    }

                    countdownJob?.cancel()
                    if (output.openAt != null && output.serverNow != null && output.minutesUntilUnlock > 0) {
                        countdownJob =
                            QuestCountdownTimer
                                .countdownFlow(output.openAt, output.serverNow)
                                .onEach { minutes -> updateTimerLockedMinutes(minutes) }
                                .onCompletion { viewModelScope.launch { unlockTimerLocked() } }
                                .launchIn(viewModelScope)
                    }
                }.onFailure {
                    _sideEffect.emit(QuestSideEffect.ShowSnackBar(CustomSnackBarType.ALERT))
                }
        }
    }

    private fun updateTimerLockedMinutes(minutes: Long) {
        _uiState.update { state ->
            val updatedGroups =
                state.myJourneyState.questGroups
                    .map { group ->
                        group.copy(
                            quests =
                                group.quests
                                    .map { quest ->
                                        if (quest.state is QuestState.TimerLocked) {
                                            quest.copy(state = quest.state.copy(remainTime = minutes))
                                        } else {
                                            quest
                                        }
                                    }.toImmutableList(),
                        )
                    }.toImmutableList()
            state.copy(myJourneyState = state.myJourneyState.copy(questGroups = updatedGroups))
        }
    }

    private fun unlockTimerLocked() {
        _uiState.update { state ->
            val updatedGroups =
                state.myJourneyState.questGroups
                    .map { group ->
                        group.copy(
                            quests =
                                group.quests
                                    .map { quest ->
                                        if (quest.state is QuestState.TimerLocked) {
                                            quest.copy(state = QuestState.Available)
                                        } else {
                                            quest
                                        }
                                    }.toImmutableList(),
                        )
                    }.toImmutableList()
            state.copy(myJourneyState = state.myJourneyState.copy(questGroups = updatedGroups))
        }
    }

    fun onQuitDismissModal() {
        _uiState.update { it.copy(myJourneyState = it.myJourneyState.copy(showQuitModal = false)) }
    }

    fun onTipClicked() {
        val quest = uiState.value.myJourneyState.selectedQuest ?: return
        viewModelScope.launch {
            mixpanelUtil.trackEvent(
                eventName = "quest_tip_pageview",
                properties = mapOf("quest_number" to quest.questNumber),
            )
            _sideEffect.emit(QuestSideEffect.NavigateToQuestTip(quest.questId, quest.type))
        }
    }

    fun onQuestStart() {
        val quest = uiState.value.myJourneyState.selectedQuest ?: return
        trackQuest(quest)
        viewModelScope.launch {
            _uiState.update { it.copy(myJourneyState = it.myJourneyState.copy(showQuitModal = false)) }
            when (quest.type) {
                QuestType.RECORDING -> _sideEffect.emit(
                    QuestSideEffect.NavigateToQuestRecording(
                        quest.questId
                    )
                )

                QuestType.ACTIVE -> _sideEffect.emit(QuestSideEffect.NavigateToQuestBehavior(quest.questId))
                else -> Unit
            }
        }
    }

    fun onQuestClicked(questId: Long) {
        viewModelScope.launch {
            val quest =
                uiState.value.myJourneyState.questGroups
                    .flatMap { it.quests }
                    .find { it.questId == questId }

            if (quest?.state is QuestState.Available) {
                _uiState.update {
                    it.copy(
                        myJourneyState = it.myJourneyState.copy(
                            selectedQuest = quest,
                            showQuitModal = true
                        )
                    )
                }
            } else if (quest?.state is QuestState.Complete) {
                handleCompletedQuestClick(quest)
            }
        }
    }

    fun onMyAnswersClicked() {
        viewModelScope.launch {
            _sideEffect.emit(QuestSideEffect.NavigateToQuestMyAnswers)
        }
    }

    fun onOtherAnswerClicked(answerId: Long) {
        viewModelScope.launch {
            val selectedAnswer =
                uiState.value.commonJourneyState.answers
                    .find { it.answerId == answerId } ?: return@launch
            val myUserId = userRepository.getUserId()

            if (selectedAnswer.writerId == myUserId) {
                _sideEffect.emit(QuestSideEffect.NavigateToQuestMyAnswersDetail(answerId))
            } else {
                mixpanelUtil.trackEvent("common_journey_others_answer_pageview")
                _sideEffect.emit(QuestSideEffect.NavigateToCommonAnswerDetail(answerId))
            }
        }
    }

    fun onCommonQuestClicked(questId: Long) {
        viewModelScope.launch {
            mixpanelUtil.trackEvent("common_journey_write_click")

            val question = _uiState.value.commonJourneyState.question
            _sideEffect.emit(QuestSideEffect.NavigateToQuestCommonWriting(questId, question))
        }
    }

    fun onMeetingBoriClicked() {
        viewModelScope.launch {
            _sideEffect.emit(QuestSideEffect.NavigateToOffboardingCompletedGuide)
        }
    }

    fun onCommonQuestCompleted() {
        savedStateHandle["selectedTab"] = QuestTab.COMMON_JOURNEY.name
        _uiState.update {
            it.copy(
                selectedTab = QuestTab.COMMON_JOURNEY,
                showCompleteModal = true,
                commonJourneyState = it.commonJourneyState.copy(isMyAnswerDone = true),
            )
        }
    }

    fun closeCompleteModal() {
        _uiState.update { it.copy(showCompleteModal = false) }
    }

    fun onHeartClicked() {
        // TODO: 하트 api 연동
    }

    private suspend fun handleCompletedQuestClick(quest: Quest) {
        mixpanelUtil.trackEvent(
            "quest_box_click",
            mapOf("quest_number" to quest.questNumber),
        )
        _sideEffect.emit(QuestSideEffect.NavigateToQuestReview(quest.questId))
    }

    private fun trackQuest(quest: Quest) {
        val questType =
            when (quest.type) {
                QuestType.RECORDING -> "질문형"
                QuestType.ACTIVE -> "행동형"
                else -> Unit
            }
        mixpanelUtil.trackEvent(
            eventName = "quest_write_pageview",
            properties =
                mapOf(
                    "quest_start_at" to getFormattedDate(),
                    "quest_number" to quest.questNumber,
                    "quest_type" to questType,
                ),
        )
    }

    private fun observeRefreshEvent() {
        viewModelScope.launch {
            commonQuestRepository.refreshEvent.collect {
                val currentDate = uiState.value.commonJourneyState.selectedDate
                refetchCommonQuests(currentDate)
            }
        }
    }

    private fun observeAnswerSubmitted() {
        viewModelScope.launch {
            commonQuestRepository.answerSubmittedEvent.collect {
                val today = TimeUtil.getNowKst()
                refetchCommonQuests(today)
                onCommonQuestCompleted()
            }
        }
    }

    private fun observeDeeplinkQuestId() {
        viewModelScope.launch {
            savedStateHandle.getStateFlow<Long?>("deeplink_quest_id", null)
                .collectLatest { questId ->
                    if (questId != null) {
                        onQuestClicked(questId)
                        savedStateHandle["deeplink_quest_id"] = null
                    }
            }
        }
    }
}
