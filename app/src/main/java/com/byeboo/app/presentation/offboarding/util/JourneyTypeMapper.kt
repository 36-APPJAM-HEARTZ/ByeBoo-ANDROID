package com.byeboo.app.presentation.offboarding.util

import com.byeboo.app.domain.model.offboarding.OffboardingJourneyType
import com.byeboo.app.presentation.offboarding.model.JourneyType

fun JourneyType.toOffboardingJourneyType(): OffboardingJourneyType = when(this){
    JourneyType.RECORDING -> OffboardingJourneyType.FACE_EMOTION
    JourneyType.ACTIVE -> OffboardingJourneyType.PROCESS_EMOTION
}
