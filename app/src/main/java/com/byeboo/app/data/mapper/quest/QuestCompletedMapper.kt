package com.byeboo.app.data.mapper.quest

import com.byeboo.app.data.dto.response.quest.QuestCompletedQuestResponseDto
import com.byeboo.app.data.dto.response.quest.QuestCompletedResponseDto
import com.byeboo.app.data.dto.response.quest.QuestCompletedStepResponseDto
import com.byeboo.app.domain.model.quest.QuestCompletedModel
import com.byeboo.app.domain.model.quest.QuestCompletedQuestModel
import com.byeboo.app.domain.model.quest.QuestCompletedStepModel

fun QuestCompletedResponseDto.toDomain(): QuestCompletedModel =
    QuestCompletedModel(
        progressPeriod = progressPeriod,
        currentStep = currentStep,
        steps = steps.orEmpty().map { it.toDomain() }
    )

fun QuestCompletedStepResponseDto.toDomain(): QuestCompletedStepModel =
    QuestCompletedStepModel(
        stepNumber = stepNumber,
        step = step,
        quests = quests.orEmpty().map { it.toDomain() }
    )

fun QuestCompletedQuestResponseDto.toDomain(): QuestCompletedQuestModel =
    QuestCompletedQuestModel(
        questId = questId,
        question = question,
        questStyle = questStyle,
        questNumber = questNumber
    )
