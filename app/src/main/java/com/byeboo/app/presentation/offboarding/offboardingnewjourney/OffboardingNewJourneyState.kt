package com.byeboo.app.presentation.offboarding.offboardingnewjourney

sealed interface OffboardingNewJourneySideEffect {
    data object NavigateToQuestStart : OffboardingNewJourneySideEffect
    data object NavigateToUp : OffboardingNewJourneySideEffect
}
