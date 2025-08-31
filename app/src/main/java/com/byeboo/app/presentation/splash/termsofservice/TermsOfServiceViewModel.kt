package com.byeboo.app.presentation.splash.termsofservice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class TermsOfServiceViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(TermsOfServiceUiState())
    val uiState: StateFlow<TermsOfServiceUiState> = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<TermsOfServiceSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    private val ALL_TERMS: Set<TermType> = TermType.entries.toSet()

    fun onTermsLinkClicked(url: String?) {
        viewModelScope.launch {
            url?.let {
                _sideEffect.emit(TermsOfServiceSideEffect.OpenUrl(it))
            }
        }
    }

    fun onAllTermsClick() {
        _uiState.update { state ->
            state.copy(
                checkedTerms = if (state.isAllChecked) emptySet() else ALL_TERMS

            )
        }
    }

    fun onTermsClick(term: TermType) {
        _uiState.update { state ->

            val terms = state.checkedTerms.toMutableSet().apply {
                if (contains(term)) remove(term) else add(term)
            }

            state.copy(checkedTerms = terms)
        }
    }

    fun onCompleteButtonClick() {
        viewModelScope.launch {
            _sideEffect.emit(TermsOfServiceSideEffect.NavigateToUserInfo)
        }
    }
}