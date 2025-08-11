package com.byeboo.app.presentation.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.byeboo.app.domain.repository.auth.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MyPageState())
    val uiState: StateFlow<MyPageState> = _uiState.asStateFlow()

    val nickname: StateFlow<String?> = userRepository.getNickname()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    fun onDismissModal(modalType: ModalType){
        when(modalType){
            ModalType.LOGOUT -> _uiState.update { it.copy(showLogoutModal = false) }
            ModalType.DELETE_ACCOUNT -> _uiState.update { it.copy(showDeleteAccountModal = false) }
        }
    }

    fun onLogoutClicked(){
        _uiState.update { it.copy(showLogoutModal = true) }
    }

    fun onDeleteAccountClicked(){
        _uiState.update { it.copy(showDeleteAccountModal = true) }
    }

    fun logout() {
        viewModelScope.launch {
            userRepository.clear()
        }
    }
}
