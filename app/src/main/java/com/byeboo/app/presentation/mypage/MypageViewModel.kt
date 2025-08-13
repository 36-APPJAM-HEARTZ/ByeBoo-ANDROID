package com.byeboo.app.presentation.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.byeboo.app.BuildConfig
import com.byeboo.app.domain.repository.auth.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val userRepository: UserRepository
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

    fun logout() {
        viewModelScope.launch {
            userRepository.clear()
        }
    }
}
