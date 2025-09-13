package com.byeboo.app.presentation.splash.termsofservice

data class TermsOfServiceUiState(
    val checkedTerms: Set<TermType> = emptySet()
) {
    companion object {
        private val ALL_TERMS = TermType.entries.toSet()
    }

    val isAllChecked: Boolean
        get() = checkedTerms == ALL_TERMS

    val nextEnabled: Boolean
        get() = isAllChecked

    fun isChecked(term: TermType): Boolean = term in checkedTerms
}

sealed interface TermsOfServiceSideEffect {
    data class OpenUrl(val url: String) : TermsOfServiceSideEffect
    data object NavigateToUserInfo : TermsOfServiceSideEffect
}
