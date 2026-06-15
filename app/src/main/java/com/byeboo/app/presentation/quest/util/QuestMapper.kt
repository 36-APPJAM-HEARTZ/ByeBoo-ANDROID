package com.byeboo.app.presentation.quest.util

import androidx.annotation.DrawableRes
import com.byeboo.app.R
import com.byeboo.app.core.model.quest.QuestType
import com.byeboo.app.core.util.TimeUtil
import com.byeboo.app.domain.model.quest.QuestDataModel
import com.byeboo.app.presentation.quest.model.CommonAnswerModel
import com.byeboo.app.presentation.quest.model.Quest
import com.byeboo.app.presentation.quest.model.QuestGroup
import com.byeboo.app.presentation.quest.model.QuestOutput
import com.byeboo.app.presentation.quest.model.QuestState
import kotlinx.collections.immutable.toImmutableList
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import javax.inject.Inject

class QuestUiModelMapper
    @Inject
    constructor() {
        fun mapToPresentationModel(data: QuestDataModel): QuestOutput {
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

        fun formatWrittenTime(writtenAt: LocalDateTime?): String {
            if (writtenAt == null) return ""

            return runCatching {
                val nowKst = TimeUtil.getNowLocalDateTimeKst()
                val todayKst = TimeUtil.getNowKst()
                val writtenDate = writtenAt.toLocalDate()

                if (writtenDate.isEqual(todayKst)) {
                    val minutes = ChronoUnit.MINUTES.between(writtenAt, nowKst)
                    val hours = ChronoUnit.HOURS.between(writtenAt, nowKst)

                    when {
                        minutes < 1 -> "방금 전"
                        minutes < 60 -> "${minutes}분 전"
                        hours < 24 -> "${hours}시간 전"
                        else -> writtenAt.format(DateTimeFormatter.ofPattern("yyyy. MM. dd."))
                    }
                } else {
                    writtenAt.format(DateTimeFormatter.ofPattern("yyyy. MM. dd."))
                }
            }.getOrDefault("")
        }

        fun mapToIconRes(iconName: String): Int = ProfileIconType.fromName(iconName).iconResId

        fun formatDetailDate(writtenAt: LocalDateTime?): String {
            if (writtenAt == null) return ""
            return runCatching {
                writtenAt.format(DateTimeFormatter.ofPattern("yyyy. MM. dd."))
            }.getOrDefault("")
        }

        fun toggleLike(answer: CommonAnswerModel): CommonAnswerModel {
            val newIsLiked = !answer.isLiked
            val newHeartCount = if (newIsLiked) answer.heartCount + 1 else answer.heartCount - 1
            return answer.copy(isLiked = newIsLiked, heartCount = newHeartCount)
        }
    }

enum class ProfileIconType(
    @DrawableRes val iconResId: Int,
) {
    SADNESS(R.drawable.ic_profile_sadness),
    SELF_UNDERSTANDING(R.drawable.ic_profile_self_understanding),
    SO_SO(R.drawable.ic_profile_so_so),
    RELIEVED(R.drawable.ic_profile_relieved),
    ;

    companion object {
        fun fromName(name: String?): ProfileIconType = entries.find { it.name == name } ?: RELIEVED
    }
}
