package com.byeboo.app.presentation.quest.tip

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.byeboo.app.core.state.UiState
import com.byeboo.app.domain.repository.quest.QuestTipRepository
import com.byeboo.app.presentation.quest.model.Quest
import com.byeboo.app.presentation.quest.navigation.QuestTip
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class QuestTipViewModel
@Inject
constructor(
    private val questTipRepository: QuestTipRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val questIdArg = savedStateHandle.toRoute<QuestTip>().questId
    private val questTypeArg = savedStateHandle.toRoute<QuestTip>().questType

    private val _uiState = MutableStateFlow<UiState<QuestTipState>>(UiState.Loading)
    val uiState: StateFlow<UiState<QuestTipState>> = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<QuestTipSideEffect>()
    val sideEffect: SharedFlow<QuestTipSideEffect> = _sideEffect.asSharedFlow()

    init {
        loadQuestTip(
            Quest(
                questId = questIdArg,
                type = questTypeArg
            )
        )
    }

    private fun loadQuestTip(quest: Quest) {
        viewModelScope.launch {
            val result = questTipRepository.getQuestTip(quest.questId)
            result
                .onSuccess { tip ->
                    _uiState.update {
                        UiState.Success(
                            QuestTipState(
                                questId = quest.questId,
                                questType = quest.type,
                                stepNumber = tip.stepNumber,
                                questNumber = tip.questNumber,
                                question = tip.question,
                                tipAnswer =
                                QuestTipAnswers(
                                    reason =
                                    tip.tips
                                        .getOrNull(0)
                                        ?.tipAnswer
                                        .orEmpty(),
                                    suggestion =
                                    tip.tips
                                        .getOrNull(1)
                                        ?.tipAnswer
                                        .orEmpty(),
                                    change =
                                    tip.tips
                                        .getOrNull(2)
                                        ?.tipAnswer
                                        .orEmpty()
                                )
                            )
                        )
                    }
                }.onFailure {
                    _uiState.update {
                        UiState.Success(
                            QuestTipState(
                                questId = questIdArg,
                                questType = questTypeArg
                            )
                        )
                    }

                    _sideEffect.emit(
                        QuestTipSideEffect.ShowSnackBar("서버에 연결할 수 없습니다. 잠시 후 시도해 주세요.")
                    )
                }
        }
    }

    fun onCloseClicked() {
        viewModelScope.launch {
            _sideEffect.emit(QuestTipSideEffect.NavigateToQuest)
        }
    }
}
