package com.byeboo.app.presentation.auth.userinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.byeboo.app.domain.model.auth.Feeling
import com.byeboo.app.domain.model.auth.NicknameValidationResult
import com.byeboo.app.domain.model.auth.NicknameValidator
import com.byeboo.app.domain.model.auth.QuestStyle
import com.byeboo.app.domain.model.auth.UserInfoModel
import com.byeboo.app.domain.model.auth.toJourneyText
import com.byeboo.app.domain.repository.auth.UserRepository
import com.byeboo.app.domain.repository.quest.QuestStateRepository
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
class UserInfoViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val questStateRepository: QuestStateRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(UserInfoState())
    val uiState: StateFlow<UserInfoState> = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<UserInfoSideEffect>()
    val sideEffect: SharedFlow<UserInfoSideEffect> = _sideEffect.asSharedFlow()

    companion object {
        private const val MAX_NICKNAME_LENGTH = 5
    }

    fun updateNickname(input: String) {
        if (input.length <= MAX_NICKNAME_LENGTH) {
            _uiState.update {
                it.copy(
                    nickname = input,
                    nicknameValidation = NicknameValidator.validate(input)
                )
            }
        }
    }

    fun updateEmotion(emotion: Feeling) {
        _uiState.update {
            it.copy(selectedEmotion = emotion)
        }
    }

    fun updateQuest(quest: QuestStyle) {
        _uiState.update {
            it.copy(selectedQuest = quest)
        }
    }

    fun resetEmotion() {
        _uiState.update {
            it.copy(selectedEmotion = null)
        }
    }

    fun resetQuest() {
        _uiState.update {
            it.copy(selectedQuest = null)
        }
    }

    fun finishUserInfo() {
        viewModelScope.launch {
            if (_uiState.value.nicknameValidation != NicknameValidationResult.Valid) return@launch

            val userInfo = UserInfoModel(
                name = _uiState.value.nickname,
                feeling = _uiState.value.selectedEmotion?.name.orEmpty(),
                questStyle = _uiState.value.selectedQuest?.name.orEmpty()
            )

            val result = userRepository.updateUserInfo(userInfo)

            if (result.isSuccess) {
                _uiState.value.selectedQuest?.let {
                    questStateRepository.updateUserJourney(it.toJourneyText())
                }
                _sideEffect.emit(UserInfoSideEffect.NavigateToLoading)
            } else {
                _sideEffect.emit(UserInfoSideEffect.ShowSnackBar("서버에 연결할 수 없습니다. 잠시 후 시도해 주세요."))
            }
        }
    }
}
