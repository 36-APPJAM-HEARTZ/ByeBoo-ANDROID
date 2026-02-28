package com.byeboo.app.presentation.quest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.byeboo.app.core.designsystem.type.CustomSnackBarType
import com.byeboo.app.core.model.quest.QuestType
import com.byeboo.app.core.util.MixpanelUtil
import com.byeboo.app.core.util.getFormattedDate
import com.byeboo.app.domain.model.quest.CommonQuestAnswer
import com.byeboo.app.domain.repository.auth.UserRepository
import com.byeboo.app.domain.usecase.QuestUseCase
import com.byeboo.app.presentation.quest.model.CommonAnswerModel
import com.byeboo.app.presentation.quest.model.Quest
import com.byeboo.app.presentation.quest.model.QuestSideEffect
import com.byeboo.app.presentation.quest.model.QuestState
import com.byeboo.app.presentation.quest.model.QuestTab
import com.byeboo.app.presentation.quest.util.QuestCountdownTimer
import com.byeboo.app.presentation.quest.util.QuestUiModelMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
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
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class QuestViewModel
    @Inject
    constructor(
        private val questUseCase: QuestUseCase,
        private val userRepository: UserRepository,
        private val mixpanelUtil: MixpanelUtil,
        private val mapper: QuestUiModelMapper,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(QuestUiState())
        val uiState: StateFlow<QuestUiState> = _uiState.asStateFlow()

        private val _sideEffect = MutableSharedFlow<QuestSideEffect>()
        val sideEffect: SharedFlow<QuestSideEffect> = _sideEffect.asSharedFlow()

        private var countdownJob: Job? = null

        init {
            viewModelScope.launch {
                userRepository.getNickname().collect { nickname ->
                    _uiState.update { it.copy(userName = nickname) }
                }
            }
            loadQuests()
            onDateChange(LocalDate.now())
        }

        private fun getTodayDummyAnswers(): ImmutableList<CommonQuestAnswer> =
            persistentListOf(
                CommonQuestAnswer(
                    answerId = 1,
                    writer = "장원영",
                    profileIcon = "SADNESS",
                    writtenAt = LocalDateTime.now().minusMinutes(0),
                    content = "헤어진 첫날 밤이었어요. 혼자 집에 있는데 갑자기 모든 게 현실로 다가왔고, 이제 정말 끝이라는 생각에 눈물이 멈추지 않았습니다.",
                ),
                CommonQuestAnswer(
                    answerId = 2,
                    writer = "아이유",
                    profileIcon = "SO_SO",
                    writtenAt = LocalDateTime.now().minusMinutes(25),
                    content = "헤어진 첫날 밤이었어요. 혼자 집에 있는데 갑자기 모든 게 현실로 다가왔고, 이제 정말 끝이라는 생각에 눈물이 멈추지 않았습니다.",
                ),
                CommonQuestAnswer(
                    answerId = 3,
                    writer = "제니",
                    profileIcon = "RELIEVED",
                    writtenAt = LocalDateTime.now().minusHours(3),
                    content = "헤어진 첫날 밤이었어요. 혼자 집에 있는데 갑자기 모든 게 현실로 다가왔고, 이제 정말 끝이라는 생각에 눈물이 멈추지 않았습니다.",
                ),
                CommonQuestAnswer(
                    answerId = 4,
                    writer = "카리나",
                    profileIcon = "SELF_UNDERSTANDING",
                    writtenAt = LocalDateTime.now().minusDays(1),
                    content = "헤어진 첫날 밤이었어요. 혼자 집에 있는데 갑자기 모든 게 현실로 다가왔고, 이제 정말 끝이라는 생각에 눈물이 멈추지 않았습니다.",
                ),
            )

        fun onTabClicked(tab: QuestTab) {
            _uiState.update { it.copy(selectedTab = tab) }
        }

        // / TODO: api 연동시 dummy가 아닌 usecase에서 호출
        fun onDateChange(newDate: LocalDate) {
            _uiState.update { currentState ->
                val isToday = newDate == LocalDate.now()
                val domainAnswers = if (isToday) getTodayDummyAnswers() else persistentListOf()

                val uiAnswers =
                    domainAnswers
                        .map { answer ->
                            CommonAnswerModel(
                                answerId = answer.answerId,
                                writer = answer.writer,
                                profileIconRes = mapper.mapToIconRes(answer.profileIcon),
                                displayTime = mapper.formatWrittenTime(answer.writtenAt),
                                content = answer.content,
                            )
                        }.toImmutableList()

                currentState.copy(
                    commonJourneyState =
                        currentState.commonJourneyState.copy(
                            selectedDate = newDate,
                            question =
                                if (isToday) {
                                    "연애에서 반복된 문제 패턴 3가지를 생각해보아요"
                                } else {
                                    "${newDate.monthValue}월 ${newDate.dayOfMonth}일의 공통 질문입니다."
                                },
                            answerCount = if (isToday) 124 else 0,
                            answers = uiAnswers,
                        ),
                )
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
