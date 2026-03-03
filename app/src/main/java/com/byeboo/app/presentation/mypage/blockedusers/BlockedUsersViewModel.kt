package com.byeboo.app.presentation.mypage.blockedusers

import android.util.Log.e
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.byeboo.app.core.designsystem.type.CustomSnackBarType
import com.byeboo.app.core.state.UiState
import com.byeboo.app.core.util.updateSuccess
import com.byeboo.app.domain.usecase.mypage.GetBlockedUsersUseCase
import com.byeboo.app.domain.usecase.mypage.UnblockUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class BlockedUsersViewModel
    @Inject
    constructor(
        private val getBlockedUsersUseCase: GetBlockedUsersUseCase,
        private val unblockUserUseCase: UnblockUserUseCase,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow<UiState<BlockedUsersState>>(UiState.Loading)
        val uiState: StateFlow<UiState<BlockedUsersState>> = _uiState.asStateFlow()

        private val _sideEffect = MutableSharedFlow<BlockedUsersSideEffect>()
        val sideEffect = _sideEffect.asSharedFlow()

        init {
            loadBlockedUsers()
        }

        private fun loadBlockedUsers() {
            viewModelScope.launch {
                getBlockedUsersUseCase()
                    .onSuccess { result ->
                        _uiState.value =
                            UiState.Success(
                                BlockedUsersState(
                                    blockedUserLists = result.blockedUsers.toImmutableList(),
                                    showBlockedModal = false,
                                ),
                            )
                    }.onFailure {
                        _uiState.value =
                            UiState.Success(
                                BlockedUsersState(
                                    blockedUserLists = persistentListOf(),
                                    showBlockedModal = false,
                                ),
                            )

                        _sideEffect.emit(
                            BlockedUsersSideEffect.ShowSnackBar(
                                snackBarType = CustomSnackBarType.ALERT,
                            ),
                        )
                    }
            }
        }

        fun onUnblockClicked(userId: Long) {
            _uiState.updateSuccess {
                it.copy(
                    selectedUserId = userId,
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

        fun unblockUser() {
            val currentState = _uiState.value as? UiState.Success ?: return
            val userId = currentState.data.selectedUserId ?: return

            Timber.d("UNBLOCK userId=$userId")

            viewModelScope.launch {
                onDismissModal()
                unblockUserUseCase(blockId = userId)
                    .onSuccess {
                        val before = ((_uiState.value as? UiState.Success)?.data?.blockedUserLists?.size)
                        Timber.d("UNBLOCK before size=$before userId=$userId")

                        _uiState.updateSuccess { state ->
                            val newList = state.blockedUserLists.filterNot { it.blockedUserId == userId }.toImmutableList()
                            Timber.d("UNBLOCK after size=${newList.size}")
                            state.copy(blockedUserLists = newList)
                        }
                    }.onFailure { e ->
                        Timber.e(e, "unblock failed")
                        _sideEffect.emit(
                            BlockedUsersSideEffect.ShowSnackBar(
                                snackBarType = CustomSnackBarType.ALERT,
                            ),
                        )
                    }
            }
        }
    }
