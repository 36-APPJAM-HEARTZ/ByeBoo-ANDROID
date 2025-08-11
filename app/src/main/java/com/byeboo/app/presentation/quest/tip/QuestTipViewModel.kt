package com.byeboo.app.presentation.quest.tip

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.byeboo.app.core.model.quest.QuestType
import com.byeboo.app.domain.repository.quest.QuestTipRepository
import com.byeboo.app.presentation.quest.model.Quest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestTipViewModel @Inject constructor(
    private val questTipRepository: QuestTipRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(QuestTipState())
    val uiState: StateFlow<QuestTipState> = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<QuestTipSideEffect>()
    val sideEffect: SharedFlow<QuestTipSideEffect> = _sideEffect

    fun onCloseClicked() {
        viewModelScope.launch {
            _sideEffect.emit(QuestTipSideEffect.NavigateToQuest)
        }
    }

    fun loadQuestTip(quest: Quest) {
        viewModelScope.launch {
            val result = questTipRepository.getQuestTip(quest.questId)
            result.onSuccess { tip ->
                _uiState.update {
                    it.copy(
                        step = QuestType.Companion.from(tip.step),
                        stepNumber = tip.stepNumber,
                        questNumber = tip.questNumber,
                        question = tip.question,
                        tipAnswer = QuestTipAnswers(
                            reason = tip.tips.getOrNull(0)?.tipAnswer ?: "",
                            suggestion = tip.tips.getOrNull(1)?.tipAnswer ?: "",
                            change = tip.tips.getOrNull(2)?.tipAnswer ?: ""

                        )
                    )
                }
            }
        }
    }
}
