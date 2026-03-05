package com.byeboo.app.presentation.quest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.byeboo.app.core.designsystem.type.CustomSnackBarType
import com.byeboo.app.core.model.quest.QuestType
import com.byeboo.app.core.util.MixpanelUtil
import com.byeboo.app.core.util.TimeUtil
import com.byeboo.app.core.util.getFormattedDate
import com.byeboo.app.domain.repository.auth.UserRepository
import com.byeboo.app.domain.repository.quest.CommonQuestRepository
import com.byeboo.app.domain.usecase.QuestUseCase
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
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class QuestViewModel
    @Inject
    constructor(
        private val questUseCase: QuestUseCase,
        private val userRepository: UserRepository,
        private val commonQuestRepository: CommonQuestRepository,
        private val mixpanelUtil: MixpanelUtil,
        private val mapper: QuestUiModelMapper,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(QuestUiState())
        val uiState: StateFlow<QuestUiState> = _uiState.asStateFlow()

        private val _sideEffect = MutableSharedFlow<QuestSideEffect>()
        val sideEffect: SharedFlow<QuestSideEffect> = _sideEffect.asSharedFlow()

        private val commonQuestCache = mutableMapOf<LocalDate, CommonJourneyState>()
        private var fetchJob: Job? = null
        private var countdownJob: Job? = null

        init {
            viewModelScope.launch {
                userRepository.getNickname().collect { nickname ->
                    _uiState.update { it.copy(userName = nickname) }
                }
            }
            loadQuests()
            onDateChange(uiState.value.commonJourneyState.selectedDate)
        }

        fun onTabClicked(tab: QuestTab) {
            _uiState.update { it.copy(selectedTab = tab) }
        }

        fun onDateChange(newDate: LocalDate) {
            if (!TimeUtil.isValidDateRange(newDate)) return

            _uiState.update { state ->
                state.copy(commonJourneyState = state.commonJourneyState.copy(selectedDate = newDate))
            }

            val cachedData = commonQuestCache[newDate]
            val isToday = newDate == TimeUtil.getNowKst()

            fetchJob?.cancel()

            if (cachedData != null && !isToday) {
                _uiState.update { it.copy(commonJourneyState = cachedData, isLoading = false) }
            } else {
                _uiState.update { it.copy(isLoading = true) }
            }

            fetchJob =
                viewModelScope.launch {
                    delay(300)
                    fetchCommonQuests(newDate)
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
                                answerId = answer.answerId,
                                writer = answer.writer,
                                profileIconRes = mapper.mapToIconRes(answer.profileIcon),
                                displayTime = mapper.formatWrittenTime(answer.writtenAt),
                                content = answer.content,
                            )
                        }.toImmutableList()

                uiState.value.commonJourneyState.copy(
                    question = domainModel.question,
                    answerCount = domainModel.answerCount.toInt(),
                    answers = uiAnswers,
                    isMyAnswerDone = domainModel.isAnswered,
                    selectedDate = date,
                )
            }.onSuccess { updatedCommonState ->
                commonQuestCache[date] = updatedCommonState
                _uiState.update { state ->
                    state.copy(isLoading = false, commonJourneyState = updatedCommonState)
                }
            }.onFailure { t ->
                _uiState.update { it.copy(isLoading = false) }

                if (commonQuestCache[date] == null) {
                    _sideEffect.emit(QuestSideEffect.ShowSnackBar(CustomSnackBarType.ALERT))
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
                                    .onEach { minutes ->
                                        updateTimerLockedMinutes(minutes)
                                    }.onCompletion {
                                        viewModelScope.launch { unlockTimerLocked() }
                                    }.launchIn(viewModelScope)
                        }
                    }.onFailure { t ->
                        _sideEffect.emit(
                            QuestSideEffect.ShowSnackBar(
                                snackBarType = CustomSnackBarType.ALERT,
                            ),
                        )
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

                state.copy(
                    myJourneyState = state.myJourneyState.copy(questGroups = updatedGroups),
                )
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

                state.copy(
                    myJourneyState = state.myJourneyState.copy(questGroups = updatedGroups),
                )
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
                    QuestType.RECORDING ->
                        _sideEffect.emit(QuestSideEffect.NavigateToQuestRecording(quest.questId))

                    QuestType.ACTIVE ->
                        _sideEffect.emit(QuestSideEffect.NavigateToQuestBehavior(quest.questId))
                }
            }
        }

        fun onQuestClicked(questId: Long) {
            viewModelScope.launch {
                val quest =
                    uiState.value.myJourneyState.questGroups
                        .flatMap { it.quests }
                        .find { it.questId == questId }

                when (quest?.state) {
                    is QuestState.Available ->
                        _uiState.update {
                            it.copy(
                                myJourneyState =
                                    it.myJourneyState.copy(
                                        selectedQuest = quest,
                                        showQuitModal = true,
                                    ),
                            )
                        }

                    is QuestState.Complete ->
                        handleCompletedQuestClick(quest)

                    else -> Unit
                }
            }
        }

        fun onMyAnswersClicked() {
            viewModelScope.launch {
                _sideEffect.emit(QuestSideEffect.NavigateToQuestMyAnswers)
            }
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
    }
