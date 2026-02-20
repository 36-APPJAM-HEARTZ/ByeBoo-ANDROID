package com.byeboo.app.presentation.mypage.blockedusers

import androidx.lifecycle.ViewModel
import com.byeboo.app.core.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class BlockedUsersViewModel
    @Inject
    constructor() : ViewModel() {
        private val _uiState = MutableStateFlow<UiState<BlockedUsersState>>(UiState.Loading)
        val uiState: StateFlow<UiState<BlockedUsersState>> = _uiState.asStateFlow()

        init {
            loadBlockedUsers()
        }

        private fun loadBlockedUsers() {
        }

        fun onUnblockClicked(userId: Long) {
        }

        fun onBackClicked() {
        }
    }
