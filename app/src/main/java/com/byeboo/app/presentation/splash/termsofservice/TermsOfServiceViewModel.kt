package com.byeboo.app.presentation.splash.termsofservice

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class TermsOfServiceViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(TermsOfServiceUiState())
    val uiState: StateFlow<TermsOfServiceUiState> = _uiState.asStateFlow()

    private val ALL_TERMS: Set<TermType> = TermType.entries.toSet()

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
}