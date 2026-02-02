package com.byeboo.app.presentation.tutorial

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

@HiltViewModel
class TutorialViewModel
@Inject
constructor() : ViewModel() {
    private val _sideEffect = MutableSharedFlow<TutorialSideEffect>()
    val sideEffect: SharedFlow<TutorialSideEffect> = _sideEffect.asSharedFlow()

    fun onBackClicked() {
        viewModelScope.launch {
            _sideEffect.emit(TutorialSideEffect.NavigateToUp)
        }
    }
}
