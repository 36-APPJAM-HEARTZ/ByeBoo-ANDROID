package com.byeboo.app.presentation.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.byeboo.app.BuildConfig
import com.byeboo.app.core.util.MixpanelUtil
import com.byeboo.app.domain.repository.auth.UserRepository
import com.byeboo.app.domain.usecase.LogoutUseCase
import com.byeboo.app.domain.usecase.WithdrawUseCase
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
class MyPageViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val logoutUseCase: LogoutUseCase,
    private val withdrawUseCase: WithdrawUseCase,
    private val mixpanelUtil: MixpanelUtil
) : ViewModel() {

    private val _uiState = MutableStateFlow(MyPageState())
    val uiState: StateFlow<MyPageState> = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<MyPageSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    init {
        viewModelScope.launch {
            userRepository.getNickname().collect { nickname ->
                _uiState.update { it.copy(nickname = nickname) }
            }
        }
    }

    fun onNicknameChangeClicked() {
        viewModelScope.launch {
            _sideEffect.emit(MyPageSideEffect.NavigateToEditProfile)
        }
    }

    fun onCompletedJourneyClicked() {
        viewModelScope.launch {
            mixpanelUtil.trackEvent("mypage_journey_review_click")
            _sideEffect.emit(MyPageSideEffect.NavigateToOffboardingCompletedJourney)
        }
    }

    fun onGoToByeBooUniverseClicked() {
        viewModelScope.launch {
            mixpanelUtil.trackEvent("tutorial_button_click")
            mixpanelUtil.trackEvent("tutorial_pageview")
            _sideEffect.emit(MyPageSideEffect.NavigateToTutorial)
        }
    }

    private fun emitOpenUrl(url: String) {
        viewModelScope.launch {
            _sideEffect.emit(MyPageSideEffect.OpenUrl(url))
        }
    }

    fun onAskingByeBooClicked() = emitOpenUrl(BuildConfig.BYEBOO_ASKING)

    fun onServiceWithByeBooClicked() = emitOpenUrl(BuildConfig.BYEBOO_SERVICE)

    fun onPrivacyPolicyClicked() = emitOpenUrl(BuildConfig.BYEBOO_PRIVACY_POLICY)

    fun onTermsOfServiceClicked() = emitOpenUrl(BuildConfig.BYEBOO_TERMS_OF_SERVICE)

    fun onDismissModal(modalType: ModalType) {
        when (modalType) {
            ModalType.LOGOUT -> _uiState.update { it.copy(showLogoutModal = false) }
            ModalType.DELETE_ACCOUNT -> _uiState.update { it.copy(showDeleteAccountModal = false) }
        }
    }

    fun onLogoutClicked() {
        _uiState.update { it.copy(showLogoutModal = true) }
    }

    fun onDeleteAccountClicked() {
        _uiState.update { it.copy(showDeleteAccountModal = true) }
    }

    fun confirmLogout() {
        viewModelScope.launch {
            logoutUseCase().onSuccess {
                mixpanelUtil.trackEvent("logout_confirm_click")
                _uiState.update { it.copy(showLogoutModal = false) }
                _sideEffect.emit(MyPageSideEffect.NavigateToSplash)
            }.onFailure {
                _sideEffect.emit(
                    MyPageSideEffect.ShowSnackBar(message = "서버에 연결할 수 없습니다. 잠시 후 시도해 주세요.")
                )
            }
        }
    }

    fun confirmWithdraw() {
        viewModelScope.launch {
            withdrawUseCase().onSuccess {
                mixpanelUtil.trackEvent("withdraw_confirm_click")
                mixpanelUtil.reset()
                _uiState.update { it.copy(showDeleteAccountModal = false) }
                _sideEffect.emit(MyPageSideEffect.NavigateToSplash)
            }.onFailure {
                _sideEffect.emit(
                    MyPageSideEffect.ShowSnackBar(message = "서버에 연결할 수 없습니다. 잠시 후 시도해 주세요.")
                )
            }
        }
    }
}
