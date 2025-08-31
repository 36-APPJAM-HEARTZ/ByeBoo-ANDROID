package com.byeboo.app.presentation.quest

import com.byeboo.app.presentation.quest.model.Quest
import com.byeboo.app.presentation.quest.model.QuestGroup
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class QuestUiState(
    val questGroups: ImmutableList<QuestGroup> = persistentListOf(),
    val progressPeriod: Long = 1L,
    val journeyTitle: String = "",
    val currentStepIndex: Int = 0,
    val userName: String = "",
    val selectedQuest: Quest? = null,
    val showQuitModal: Boolean = false,
    val completedQuestCount: Long = 1L,
    val showOffboardingModal: Boolean = false,
    val error: String? = null
)
