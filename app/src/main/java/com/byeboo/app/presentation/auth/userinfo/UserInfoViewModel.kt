package com.byeboo.app.presentation.auth.userinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.byeboo.app.core.designsystem.type.CustomSnackBarType
import com.byeboo.app.core.model.quest.JourneyType
import com.byeboo.app.core.util.MixpanelUtil
import com.byeboo.app.domain.model.auth.BadWordValidator
import com.byeboo.app.domain.model.auth.NicknameValidationResult
import com.byeboo.app.domain.model.auth.NicknameValidator
import com.byeboo.app.domain.model.auth.UserInfoModel
import com.byeboo.app.domain.model.notification.FcmTokenModel
import com.byeboo.app.domain.repository.auth.UserRepository
import com.byeboo.app.domain.repository.fcm.FcmTokenRepository
import com.byeboo.app.domain.repository.quest.QuestStateRepository
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class UserInfoViewModel
    @Inject
    constructor(
        private val userRepository: UserRepository,
        private val questStateRepository: QuestStateRepository,
        private val fcmTokenRepository: FcmTokenRepository,
        private val badWordValidator: BadWordValidator,
        private val mixpanelUtil: MixpanelUtil,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(UserInfoState())
        val uiState: StateFlow<UserInfoState> = _uiState.asStateFlow()

        private val _sideEffect = MutableSharedFlow<UserInfoSideEffect>()
        val sideEffect: SharedFlow<UserInfoSideEffect> = _sideEffect.asSharedFlow()

        private var hasSubmitted = false

        companion object {
            private const val MAX_NICKNAME_LENGTH = 5
        }

        fun updateNickname(input: String) {
            if (input.length <= MAX_NICKNAME_LENGTH) {
                _uiState.update {
                    it.copy(
                        nickname = input,
                        nicknameValidation = NicknameValidator.validate(input),
                    )
                }
            }
        }

        fun onNicknameComplete() {
            val nickname = _uiState.value.nickname

            if (badWordValidator.contains(nickname)) {
                viewModelScope.launch {
                    _sideEffect.emit(
                        UserInfoSideEffect.ShowSnackBar(CustomSnackBarType.BAD_WORD),
                    )
                }
                return
            }

            mixpanelUtil.trackEvent("nickname_complete")
            viewModelScope.launch {
                _sideEffect.emit(UserInfoSideEffect.NavigateToNextPage)
            }
        }

        fun updateQuest(quest: JourneyType) {
            _uiState.update {
                it.copy(selectedQuest = quest)
            }
        }

        fun finishUserInfo() {
            if (hasSubmitted) return
            val currentState = _uiState.value

            val supportedJourneys =
                setOf(
                    JourneyType.RECORDING,
                    JourneyType.REUNION,
                )

            if (currentState.nicknameValidation != NicknameValidationResult.Valid || currentState.selectedQuest == null) {
                return
            }

            if (currentState.selectedQuest !in supportedJourneys) {
                viewModelScope.launch {
                    _sideEffect.emit(UserInfoSideEffect.ShowSnackBar(CustomSnackBarType.ALERT))
                }
                return
            }

            hasSubmitted = true

            viewModelScope.launch {
                val userInfo =
                    UserInfoModel(
                        name = currentState.nickname,
                        questStyle = currentState.selectedQuest.name,
                    )

                val result = userRepository.updateUserInfo(userInfo)

                if (result.isSuccess) {
                    questStateRepository.updateUserJourney(currentState.selectedQuest.journeyName)
                    userRepository.setUserRegistered(true)

                    saveFcmToken()

                    if (fcmTokenRepository.isAlarmEnabled()) {
                        fcmTokenRepository.allowQuestAlarm()
                    }
                    _sideEffect.emit(UserInfoSideEffect.NavigateToLoading)
                } else {
                    hasSubmitted = false
                    _sideEffect.emit(
                        UserInfoSideEffect.ShowSnackBar(CustomSnackBarType.ALERT),
                    )
                }
            }
        }

        private fun saveFcmToken() {
            FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@addOnCompleteListener
                }

                val token = task.result
                viewModelScope.launch {
                    withContext(NonCancellable) {
                        fcmTokenRepository
                            .saveFcmToken(FcmTokenModel(token))
                            .onSuccess { Timber.d("Fcm 토큰 성공: $token") }
                            .onFailure { Timber.e(it, "Fcm 토큰 실패") }
                    }
                }
            }
        }
    }
