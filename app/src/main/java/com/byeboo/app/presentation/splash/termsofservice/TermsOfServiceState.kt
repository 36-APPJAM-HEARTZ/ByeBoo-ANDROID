package com.byeboo.app.presentation.splash.termsofservice

data class TermsOfServiceUiState(
    val checkedTerms: Set<TermType> = emptySet()
) {
    val isAllChecked: Boolean
        get() = TermType.entries
            .all { checkedTerms.contains(it) }

    val nextEnabled: Boolean
        get() = TermType.entries
            .all { checkedTerms.contains(it) }

    fun isChecked(term: TermType): Boolean = checkedTerms.contains(term)
}