package com.byeboo.app.data.mapper.quest

import com.byeboo.app.data.dto.response.quest.QuestRecordedDetailResponseDto
import com.byeboo.app.domain.model.quest.QuestRecordedDetailModel

fun QuestRecordedDetailResponseDto.toDomain(): QuestRecordedDetailModel =
    QuestRecordedDetailModel(
        stepNumber = this.stepNumber,
        questNumber = this.questNumber,
        createdAt = this.createdAt,
        question = this.question,
        questAnswer = this.answer,
        questEmotionState = this.questEmotionState,
        imageKey = this.imageKey,
        imageUrl = this.imageUrl,
        emotionDescription = this.emotionDescription,
    )
