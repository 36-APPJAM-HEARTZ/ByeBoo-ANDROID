package com.byeboo.app.presentation.offboarding.offboardingnewjourney

import com.byeboo.app.core.model.quest.JourneyType

sealed interface OffboardingNewJourneySideEffect {
    data class NavigateToQuestStart(
        val journey: JourneyType?,
    ) : OffboardingNewJourneySideEffect

    data object NavigateUp : OffboardingNewJourneySideEffect
}
