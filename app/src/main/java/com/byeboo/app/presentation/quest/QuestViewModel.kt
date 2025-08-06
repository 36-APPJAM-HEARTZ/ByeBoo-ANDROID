package com.byeboo.app.presentation.quest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.byeboo.app.core.model.quest.QuestType
import com.byeboo.app.domain.usecase.QuestUseCase
import com.byeboo.app.presentation.quest.model.Quest
import com.byeboo.app.presentation.quest.model.QuestGroup
import com.byeboo.app.presentation.quest.model.QuestSideEffect
import com.byeboo.app.presentation.quest.model.QuestState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class QuestViewModel @Inject constructor(
    private val questUseCase: QuestUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(QuestUiState())
    val uiState: StateFlow<QuestUiState> = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<QuestSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    init {
        loadQuests()
    }

    private fun loadQuests() {
        viewModelScope.launch {
            val data = questUseCase()

            val currentStep = data.inProgressQuest.currentStep
            val steps = data.inProgressQuest.steps

            val questGroups = steps.map { step ->
                QuestGroup(
                    questNumber = step.stepNumber,
                    stepTitle = step.stepTitle,
                    quests = step.quests.map { quest ->
                        val state = when {
                            quest.questNumber < currentStep -> QuestState.Complete
                            quest.questNumber == currentStep.toLong() -> QuestState.Available
                            else -> QuestState.Locked
                        }
                        Quest(
                            questId = quest.questId,
                            questNumber = quest.questNumber,
                            state = state,
                            questQuestion = quest.question,
                            type = QuestType.from(quest.questStyle)
                        )
                    }
                )
            }

            val currentStepIndex = questGroups.indexOfFirst {
                it.quests.any { it.state is QuestState.Available }
            }.coerceAtLeast(0)

            _uiState.update {
                it.copy(
                    questGroups = questGroups,
                    currentStepIndex = currentStepIndex,
                    progressPeriod = data.inProgressQuest.progressPeriod,
                    journeyTitle = data.journeyTitle,
                    userName = data.userNickname
                )
            }
        }
    }

    fun onQuestClick(questId: Long) {
        viewModelScope.launch {
            val quest = uiState.value.questGroups
                .flatMap { it.quests }
                .find { it.questId == questId }

            when (quest?.state) {
                is QuestState.Available -> {
                    _uiState.update { it.copy(selectedQuest = quest, showQuitModal = true) }
                }

                is QuestState.Complete -> {
                    _sideEffect.emit(
                        QuestSideEffect.NavigateToQuestReview(questId = quest.questId)
                    )
                }

                else -> {
                }
            }
        }
    }

    fun onDismissModal() {
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
                QuestType.RECORDING -> _sideEffect.emit(
                    QuestSideEffect.NavigateToQuestRecording(quest.questId)
                )

                QuestType.ACTIVE -> _sideEffect.emit(
                    QuestSideEffect.NavigateToQuestBehavior(quest.questId)
                )
            }
        }
    }
}