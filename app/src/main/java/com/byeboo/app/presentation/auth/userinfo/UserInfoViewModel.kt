package com.byeboo.app.presentation.auth.userinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.byeboo.app.core.designsystem.type.CustomSnackBarType
import com.byeboo.app.core.util.MixpanelUtil
import com.byeboo.app.domain.model.auth.Feeling
import com.byeboo.app.domain.model.auth.NicknameValidationResult
import com.byeboo.app.domain.model.auth.NicknameValidator
import com.byeboo.app.domain.model.auth.QuestStyle
import com.byeboo.app.domain.model.auth.UserInfoModel
import com.byeboo.app.domain.model.auth.toJourneyText
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
            mixpanelUtil.trackEvent("nickname_complete")
        }

        fun updateEmotion(emotion: Feeling) {
            _uiState.update {
                it.copy(selectedEmotion = emotion)
            }
        }

        fun onCurrentEmotionComplete() {
            mixpanelUtil.trackEvent("current_emotion_complete")
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

        private fun trackQuestSelected(questStyle: QuestStyle) {
            mixpanelUtil.trackEvent(
                eventName = "quest_type_complete",
                properties =
                    mapOf(
                        "quest_type" to
                            when (questStyle) {
                                QuestStyle.RECORDING -> "질문형"
                                QuestStyle.ACTIVE -> "행동형"
                            },
                    ),
            )
        }

        fun finishUserInfo() {
            if (hasSubmitted) return
            hasSubmitted = true

            viewModelScope.launch {
                if (_uiState.value.nicknameValidation != NicknameValidationResult.Valid) {
                    hasSubmitted = false
                    return@launch
                }

                val userInfo =
                    UserInfoModel(
                        name = _uiState.value.nickname,
                        feeling =
                            _uiState.value.selectedEmotion
                                ?.name
                                .orEmpty(),
                        questStyle =
                            _uiState.value.selectedQuest
                                ?.name
                                .orEmpty(),
                    )

                val result = userRepository.updateUserInfo(userInfo)

                if (result.isSuccess) {
                    _uiState.value.selectedQuest?.let { selectedQuest ->
                        trackQuestSelected(selectedQuest)
                        questStateRepository.updateUserJourney(selectedQuest.toJourneyText())
                        userRepository.setUserRegistered(true)
                    }
                    saveFcmToken()

                    val isRegisteredUser = fcmTokenRepository.isAlarmEnabled()
                    if (isRegisteredUser) {
                        fcmTokenRepository.allowQuestAlarm()
                    }
                    _sideEffect.emit(UserInfoSideEffect.NavigateToLoading)
                } else {
                    hasSubmitted = false
                    _sideEffect.emit(
                        UserInfoSideEffect.ShowSnackBar(
                            "서버에 연결할 수 없습니다. 잠시 후 시도해 주세요.",
                            CustomSnackBarType.ALERT
                        ),
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
