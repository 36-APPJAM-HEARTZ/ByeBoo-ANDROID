package com.byeboo.app.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.byeboo.app.domain.repository.auth.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _sideEffect = MutableSharedFlow<SplashState>()
    val sideEffect = _sideEffect.asSharedFlow()

    init {
        checkUserStatusAndNavigate()
    }

    // TODO: 소셜 로그인 생기면 로직 변경 예정
    private fun checkUserStatusAndNavigate() {
        viewModelScope.launch {
            delay(1000)

            val isLoggedIn = userRepository.isLoggedIn()

            val effect = if (isLoggedIn) {
                SplashState.NavigateToHome
            } else {
                SplashState.NavigateToUserInfo
            }

            _sideEffect.emit(effect)
        }
    }
}
