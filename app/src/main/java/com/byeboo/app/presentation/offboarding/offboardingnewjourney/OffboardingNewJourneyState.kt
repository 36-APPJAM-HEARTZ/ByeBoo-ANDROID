package com.byeboo.app.presentation.offboarding.offboardingnewjourney

import com.byeboo.app.domain.model.offboarding.JourneyType

data class OffboardingNewJourneyState (
    val unCompleted: Int = 0,
    val completed: Int = 0,
    val journeyType: JourneyType = JourneyType.QUESTION
)
