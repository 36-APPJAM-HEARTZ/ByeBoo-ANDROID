package com.byeboo.app.presentation.auth.loading

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.byeboo.app.domain.repository.auth.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class LoadingViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _sideEffect = MutableSharedFlow<LoadingSideEffect>()
    val sideEffect: SharedFlow<LoadingSideEffect> = _sideEffect.asSharedFlow()

    val nickname: StateFlow<String?> = userRepository.getNickname()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    init {
        viewModelScope.launch {
            delay(1500)
            _sideEffect.emit(LoadingSideEffect.NavigateToHomeAmulet)
        }
    }
}
