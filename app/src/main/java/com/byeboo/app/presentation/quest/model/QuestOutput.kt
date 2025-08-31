package com.byeboo.app.presentation.quest.model

import kotlinx.collections.immutable.ImmutableList
import java.time.Instant

data class QuestOutput(
    val questGroups: ImmutableList<QuestGroup>,
    val activeStepIndex: Int,
    val minutesUntilUnlock: Long,
    val openAt: Instant?,
    val serverNow: Instant?,
    val progressPeriod: Long,
    val journeyTitle: String,
    val userName: String,
    val questCompletedCount: Long
)
