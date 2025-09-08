package com.byeboo.app.presentation.quest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.byeboo.app.core.model.quest.QuestType
import com.byeboo.app.domain.repository.auth.UserRepository
import com.byeboo.app.domain.usecase.QuestUseCase
import com.byeboo.app.presentation.quest.model.QuestSideEffect
import com.byeboo.app.presentation.quest.model.QuestState
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
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestViewModel @Inject constructor(
    private val questUseCase: QuestUseCase,
    private val mapper: QuestUiModelMapper,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(QuestUiState())
    val uiState: StateFlow<QuestUiState> = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<QuestSideEffect>()
    val sideEffect: SharedFlow<QuestSideEffect> = _sideEffect.asSharedFlow()

    private var countdownJob: Job? = null

    init {
        viewModelScope.launch {
            userRepository.getNickname().collect { nickname ->
                _uiState.update {
                    it.copy(userName = nickname)
                }
            }
        }
        loadQuests()
    }

    private fun loadQuests() {
        viewModelScope.launch {
            runCatching { questUseCase() }
                .mapCatching { data -> mapper.mapToPresentationModel(data) }
                .onSuccess { output ->
                    _uiState.update {
                        it.copy(
                            questGroups = output.questGroups,
                            currentStepIndex = output.activeStepIndex,
                            progressPeriod = output.progressPeriod,
                            journeyTitle = output.journeyTitle,
                            completedQuestCount = output.questCompletedCount,
                            error = null
                        )
                    }

                    countdownJob?.cancel()
                    if (true) { // 조건을 테스트용으로 강제로 true
                        countdownJob = (3 downTo 1).asFlow() // 30, 29, 28 ...
                            .onEach { seconds ->
                                delay(1000) // 1초마다 방출
                                updateTimerLockedMinutes(seconds.toLong())
                            }
                            .onCompletion {
                                viewModelScope.launch { unlockTimerLocked() }
                            }
                            .launchIn(viewModelScope)
                    }
//                    if (output.openAt != null && output.serverNow != null && output.minutesUntilUnlock > 0) {
//                        countdownJob = QuestCountdownTimer
//                            .countdownFlow(output.openAt, output.serverNow)
//                            .onEach { minutes ->
//                                updateTimerLockedMinutes(minutes)
//                            }
//                            .onCompletion {
//                                viewModelScope.launch { unlockTimerLocked() }
//                            }
//                            .launchIn(viewModelScope)
//                    }
                }
                .onFailure { t ->
                    // TODO: 추후 수정 예정
                    _uiState.update { it.copy(error = t.message ?: "알 수 없는 오류가 발생했어요") }
                }
        }
    }

    private fun updateTimerLockedMinutes(minutes: Long) {
        _uiState.update { state ->
            state.copy(
                questGroups = state.questGroups.map { group ->
                    group.copy(
                        quests = group.quests.map { quest ->
                            if (quest.state is QuestState.TimerLocked)
                                quest.copy(state = quest.state.copy(remainTime = minutes))
                            else quest
                        }.toImmutableList()
                    )
                }.toImmutableList()
            )
        }
    }

    private fun unlockTimerLocked() {
        _uiState.update { state ->
            state.copy(
                questGroups = state.questGroups.map { group ->
                    group.copy(
                        quests = group.quests.map { quest ->
                            if (quest.state is QuestState.TimerLocked)
                                quest.copy(state = QuestState.Available)
                            else quest
                        }.toImmutableList()
                    )
                }.toImmutableList()
            )
        }
    }

    fun onQuitDismissModal() {
        _uiState.update { it.copy(showQuitModal = false) }
    }

    fun onTipClick() {
        val quest = uiState.value.selectedQuest ?: return
        viewModelScope.launch {
            _sideEffect.emit(QuestSideEffect.NavigateToQuestTip(quest.questId, quest.type))
        }
    }

    fun onQuestStart() {
        val quest = uiState.value.selectedQuest ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(showQuitModal = false) }
            when (quest.type) {
                QuestType.RECORDING ->
                    _sideEffect.emit(QuestSideEffect.NavigateToQuestRecording(quest.questId))

                QuestType.ACTIVE ->
                    _sideEffect.emit(QuestSideEffect.NavigateToQuestBehavior(quest.questId))
            }
        }
    }

    fun onQuestClick(questId: Long) {
        viewModelScope.launch {
            val quest = uiState.value.questGroups
                .flatMap { it.quests }
                .find { it.questId == questId }

            when (quest?.state) {
                is QuestState.Available ->
                    _uiState.update { it.copy(selectedQuest = quest, showQuitModal = true) }

                is QuestState.Complete ->
                    _sideEffect.emit(QuestSideEffect.NavigateToQuestReview(quest.questId))

                else -> Unit
            }
        }
    }
}
