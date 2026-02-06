package com.byeboo.app.presentation.auth.loading

sealed interface LoadingSideEffect {
    data object NavigateToHomeAmulet : LoadingSideEffect
}
