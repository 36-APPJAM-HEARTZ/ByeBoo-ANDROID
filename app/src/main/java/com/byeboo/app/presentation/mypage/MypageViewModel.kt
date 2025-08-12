package com.byeboo.app.presentation.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.byeboo.app.domain.repository.auth.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
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

    fun onAskingByeBooClicked() = emitOpenUrl(Urls.ASKING_BYEBOO)

    fun onServiceWithByeBooClicked() = emitOpenUrl(Urls.SERVICE)

    fun onPrivacyPolicyClicked() = emitOpenUrl(Urls.PRIVACY_POLICY)

    fun onTermsOfServiceClicked() = emitOpenUrl(Urls.TERMS_OF_SERVICE)

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

    object Urls {
        const val ASKING_BYEBOO = "https://forms.gle/AhqzzkHKWYAgo4m96"
        const val SERVICE = "https://forms.gle/BA77gAgZ1NCatart5"
        const val PRIVACY_POLICY =
            "https://www.notion.so/24cab823e68d80a19ab1fbf87d6cfbc3?source=copy_link"
        const val TERMS_OF_SERVICE =
            "https://www.notion.so/24cab823e68d801aac95ec5d0389d192?source=copy_link"
    }
}
