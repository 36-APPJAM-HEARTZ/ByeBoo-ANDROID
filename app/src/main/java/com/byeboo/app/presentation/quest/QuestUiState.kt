package com.byeboo.app.presentation.quest

import com.byeboo.app.core.util.TimeUtil
import com.byeboo.app.domain.model.JourneyStatusType
import com.byeboo.app.presentation.quest.model.CommonAnswerModel
import com.byeboo.app.presentation.quest.model.Quest
import com.byeboo.app.presentation.quest.model.QuestGroup
import com.byeboo.app.presentation.quest.model.QuestTab
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import java.time.LocalDate

data class QuestUiState(
    val selectedTab: QuestTab = QuestTab.MY_JOURNEY,
    val userName: String = "",
    val error: String? = null,
    val isLoading: Boolean = false,
    val myJourneyState: MyJourneyState = MyJourneyState(),
    val commonJourneyState: CommonJourneyState = CommonJourneyState(),
    val showCompleteModal: Boolean = false,
)

data class MyJourneyState(
    val questGroups: ImmutableList<QuestGroup> = persistentListOf(),
    val progressPeriod: Long = 1L,
    val journeyTitle: String = "",
    val journeyStatus: JourneyStatusType = JourneyStatusType.BEFORE_START,
    val currentStepIndex: Int = 0,
    val selectedQuest: Quest? = null,
    val showQuitModal: Boolean = false,
    val completedQuestCount: Long = 1L,
)

data class CommonJourneyState(
    val selectedDate: LocalDate = TimeUtil.getNowKst(),
    val question: String = "",
    val questId: Long = 0L,
    val answerCount: Int = 0,
    val answers: ImmutableList<CommonAnswerModel> = persistentListOf(),
    val isMyAnswerDone: Boolean = false,
    val hasNext: Boolean = false,
    val nextCursor: Long? = null,
)
