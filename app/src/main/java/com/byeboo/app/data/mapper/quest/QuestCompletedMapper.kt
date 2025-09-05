package com.byeboo.app.data.mapper.quest

import com.byeboo.app.data.dto.response.quest.QuestCompletedResponseDto
import com.byeboo.app.domain.model.quest.QuestCompletedModel
import com.byeboo.app.domain.model.quest.QuestCompletedQuestModel
import com.byeboo.app.domain.model.quest.QuestCompletedStepModel

fun QuestCompletedResponseDto.toDomain(): QuestCompletedModel {
    return QuestCompletedModel(
        progressPeriod = progressPeriod,
        currentStep = currentStep,
        steps = steps.map { stepDto ->
            QuestCompletedStepModel(
                stepNumber = stepDto.stepNumber,
                step = stepDto.step,
                quests = stepDto.quests.map { questDto ->
                    QuestCompletedQuestModel(
                        questId = questDto.questId,
                        question = questDto.question,
                        questStyle = questDto.questStyle,
                        questNumber = questDto.questNumber
                    )
                }
            )
        }
    )
}
