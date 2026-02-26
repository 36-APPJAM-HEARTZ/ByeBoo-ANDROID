package com.byeboo.app.presentation.mypage.blockedusers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.byeboo.app.core.state.UiState
import com.byeboo.app.core.util.updateSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BlockedUsersViewModel
    @Inject
    constructor() : ViewModel() {
        private val _uiState = MutableStateFlow<UiState<BlockedUsersState>>(UiState.Loading)
        val uiState: StateFlow<UiState<BlockedUsersState>> = _uiState.asStateFlow()

        private val _sideEffect = MutableSharedFlow<BlockedUsersSideEffect>()
        val sideEffect = _sideEffect.asSharedFlow()

        init {
            loadBlockedUsers()
        }

        private fun loadBlockedUsers() {
            viewModelScope.launch {
                _uiState.value =
                    UiState.Success(
                        BlockedUsersState(
                            userLists = persistentListOf(), // 빈 리스트
                            showBlockedModal = false,
                        ),
                    )
            }
        }

        // TODO: 서버 연결할 때, userId 관련 코드 수정 예정
        fun onUnblockClicked(userId: Long) {
            _uiState.updateSuccess {
                it.copy(
                    showBlockedModal = true,
                )
            }
        }

        fun onBackClicked() {
            viewModelScope.launch {
                _sideEffect.emit(
                    BlockedUsersSideEffect.NavigateUp,
                )
            }
        }

        fun onDismissModal() {
            _uiState.updateSuccess {
                it.copy(
                    showBlockedModal = false,
                )
            }
        }

        fun fetchBlockedUser() {
        }
    }
