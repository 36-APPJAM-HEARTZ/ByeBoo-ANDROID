package com.byeboo.app.presentation.quest.util

import com.byeboo.app.R
import com.byeboo.app.core.model.quest.QuestType
import com.byeboo.app.domain.model.quest.QuestData
import com.byeboo.app.presentation.quest.model.Quest
import com.byeboo.app.presentation.quest.model.QuestGroup
import com.byeboo.app.presentation.quest.model.QuestOutput
import com.byeboo.app.presentation.quest.model.QuestState
import kotlinx.collections.immutable.toImmutableList
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import javax.inject.Inject

class QuestUiModelMapper
    @Inject
    constructor() {
        fun mapToPresentationModel(data: QuestData): QuestOutput {
            val inProgress = data.inProgressQuest
            val currentStep = inProgress.currentStep
            val serverNow = inProgress.currentTime
            val openAt = inProgress.questOpenTime
            val minutesUntilUnlock = remainingMinutes(openAt, serverNow)

            val groups =
                inProgress.steps
                    .map { step ->
                        QuestGroup(
                            stepNumber = step.stepNumber,
                            stepTitle = step.stepTitle,
                            quests =
                                step.quests
                                    .map { quest ->
                                        val state =
                                            when {
                                                quest.questNumber < currentStep.toLong() -> QuestState.Complete
                                                quest.questNumber == currentStep.toLong() && minutesUntilUnlock > 0 ->
                                                    QuestState.TimerLocked(
                                                        remainTime = minutesUntilUnlock,
                                                    )

                                                quest.questNumber == currentStep.toLong() -> QuestState.Available
                                                else -> QuestState.Locked
                                            }
                                        Quest(
                                            questId = quest.questId,
                                            questNumber = quest.questNumber,
                                            questQuestion = quest.question,
                                            state = state,
                                            type = QuestType.fromQuestStyle(quest.questStyle),
                                        )
                                    }.toImmutableList(),
                        )
                    }.toImmutableList()

            val activeIndex =
                groups
                    .indexOfFirst {
                        it.quests.any { quest -> quest.state is QuestState.Available || quest.state is QuestState.TimerLocked }
                    }.coerceAtLeast(0)

            return QuestOutput(
                questGroups = groups,
                activeStepIndex = activeIndex,
                minutesUntilUnlock = minutesUntilUnlock,
                openAt = openAt,
                serverNow = serverNow,
                progressPeriod = inProgress.progressPeriod,
                journeyTitle = data.journeyTitle,
                questCompletedCount = data.questCompletedCount,
            )
        }

        private fun remainingMinutes(
            openAt: Instant?,
            now: Instant?,
        ): Long {
            if (openAt == null || now == null) return 0
            return Duration.between(now, openAt).toMinutes().coerceAtLeast(0)
        }
        fun formatWrittenTime(writtenAt: LocalDateTime): String {
            val now = LocalDateTime.now()
            val today = LocalDate.now()
            val writtenDate = writtenAt.toLocalDate()

            return if (writtenDate.isEqual(today)) {
                val minutes = ChronoUnit.MINUTES.between(writtenAt, now)
                val hours = ChronoUnit.HOURS.between(writtenAt, now)

                when {
                    minutes < 60 -> {
                        if (minutes < 1) "1분 전" else "${minutes}분 전"
                    }
                    else -> "${hours}시간 전"
                }
            } else {
                writtenAt.format(DateTimeFormatter.ofPattern("yyyy. MM. dd."))
            }
        }

        fun mapToIconRes(iconName: String): Int {
            return when (iconName) {
                "SADNESS" -> R.drawable.ic_profile_sadness
                "SELF_UNDERSTANDING" -> R.drawable.ic_profile_self_understanding
                "SO_SO" -> R.drawable.ic_profile_so_so
                "RELIEVED" -> R.drawable.ic_profile_relieved
                else -> R.drawable.ic_profile_relieved
            }
        }

    }
