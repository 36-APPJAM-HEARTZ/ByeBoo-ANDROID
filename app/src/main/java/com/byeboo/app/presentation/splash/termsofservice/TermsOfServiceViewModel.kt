package com.byeboo.app.presentation.splash.termsofservice

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class TermsOfServiceViewModel () : ViewModel() {


    private val _uiState = MutableStateFlow(TermsOfServiceUiState())
    val uiState: StateFlow<TermsOfServiceUiState> = _uiState.asStateFlow()


    fun onAllTermsClick() {
        _uiState.update {
            it.copy(
                checkedTerms = if (uiState.value.isAllChecked) {
                    emptySet()
                } else{
                    TermType.entries.toSet()
                }
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