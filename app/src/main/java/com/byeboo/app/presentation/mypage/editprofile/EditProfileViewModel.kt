package com.byeboo.app.presentation.mypage.editprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.byeboo.app.core.designsystem.type.CustomSnackBarType
import com.byeboo.app.domain.model.auth.NicknameValidationResult
import com.byeboo.app.domain.model.auth.NicknameValidator
import com.byeboo.app.domain.repository.auth.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel
    @Inject
    constructor(
        val userRepository: UserRepository,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(EditProfileState())
        val uiState: StateFlow<EditProfileState> = _uiState.asStateFlow()

        private val _sideEffect = MutableSharedFlow<EditProfileSideEffect>()
        val sideEffect: SharedFlow<EditProfileSideEffect> = _sideEffect.asSharedFlow()

        companion object {
            private const val MAX_NICKNAME_LENGTH = 5
        }

        init {
            viewModelScope.launch {
                val initialNickname = userRepository.getNickname().first()
                _uiState.update {
                    it.copy(
                        nickname = initialNickname,
                        initialNickname = initialNickname,
                        isInitial = true,
                    )
                }
            }
        }

        fun updateNickname(input: String) {
            if (input.length <= MAX_NICKNAME_LENGTH) {
                _uiState.update {
                    it.copy(
                        nickname = input,
                        nicknameValidation = NicknameValidator.validate(input),
                        isInitial = it.isInitial && (input == it.initialNickname),
                    )
                }
            }
        }

        fun onBackClicked() {
            val nickname = uiState.value.nickname
            viewModelScope.launch {
                _sideEffect.emit(EditProfileSideEffect.NavigateToMyPage(nickname))
            }
        }

        fun finishEditProfile(nickname: String) {
            viewModelScope.launch {
                if (NicknameValidator.validate(nickname) != NicknameValidationResult.Valid) return@launch

                userRepository
                    .updateUserNickname(nickname)
                    .onSuccess {
                        _sideEffect.emit(EditProfileSideEffect.NavigateToMyPage(nickname))
                    }.onFailure {
                        _sideEffect.emit(
                            EditProfileSideEffect.ShowSnackBar(
                                message = "서버에 연결할 수 없습니다. 잠시 후 시도해 주세요.",
                                iconType = CustomSnackBarType.ALERT),
                        )
                    }
            }
        }
    }
