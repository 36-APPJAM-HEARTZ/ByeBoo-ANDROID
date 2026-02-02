package com.byeboo.app.presentation.offboarding.offboardingnewjourney

import com.byeboo.app.core.model.quest.QuestType

sealed interface OffboardingNewJourneySideEffect {
    data class NavigateToQuestStart(
        val journey: QuestType?,
    ) : OffboardingNewJourneySideEffect

    data object NavigateUp : OffboardingNewJourneySideEffect
}
