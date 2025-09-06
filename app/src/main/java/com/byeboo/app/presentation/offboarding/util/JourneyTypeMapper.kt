package com.byeboo.app.presentation.offboarding.util

import com.byeboo.app.core.model.quest.QuestType
import com.byeboo.app.domain.model.offboarding.OffboardingJourneyType

fun QuestType.toOffboardingJourneyType(): OffboardingJourneyType = when(this){
    QuestType.RECORDING -> OffboardingJourneyType.FACE_EMOTION
    QuestType.ACTIVE -> OffboardingJourneyType.PROCESS_EMOTION
}
