package com.byeboo.app.data.mapper.quest

import com.byeboo.app.data.dto.response.quest.QuestInProgressResponseDto
import com.byeboo.app.domain.model.quest.QuestInProgressModel
import com.byeboo.app.domain.model.quest.QuestItemModel
import com.byeboo.app.domain.model.quest.QuestStepModel
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun QuestInProgressResponseDto.toDomain(): QuestInProgressModel =
    QuestInProgressModel(
        progressPeriod = progressPeriod,
        currentStep = currentStep,
        questOpenTime = parseServerTimeToInstant(questOpenTime),
        currentTime = parseServerTimeToInstant(currentTime),
        steps =
            steps.map { stepDto ->
                QuestStepModel(
                    stepNumber = stepDto.stepNumber,
                    stepTitle = stepDto.step,
                    quests =
                        stepDto.quests.map { questDto ->
                            QuestItemModel(
                                questId = questDto.questId,
                                question = questDto.question,
                                questStyle = questDto.questStyle,
                                questNumber = questDto.questNumber,
                            )
                        },
                )
            },
    )

fun parseServerTimeToInstant(serverResponse: String?): Instant? {
    if (serverResponse.isNullOrBlank()) return null
    val localTime = LocalDateTime.parse(serverResponse, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    return localTime.atZone(ZoneId.systemDefault()).toInstant()
}
