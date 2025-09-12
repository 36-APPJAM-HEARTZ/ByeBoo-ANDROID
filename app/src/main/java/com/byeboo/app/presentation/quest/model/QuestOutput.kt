package com.byeboo.app.presentation.quest.model

import java.time.Instant
import kotlinx.collections.immutable.ImmutableList

data class QuestOutput(
    val questGroups: ImmutableList<QuestGroup>,
    val activeStepIndex: Int,
    val minutesUntilUnlock: Long,
    val openAt: Instant?,
    val serverNow: Instant?,
    val progressPeriod: Long,
    val journeyTitle: String,
    val questCompletedCount: Long
)
