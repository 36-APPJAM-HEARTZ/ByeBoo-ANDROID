package com.byeboo.app.data.mapper.quest

import com.byeboo.app.data.dto.response.quest.QuestRecordedDetailResponseDto
import com.byeboo.app.domain.model.quest.QuestRecordedDetailModel

fun QuestRecordedDetailResponseDto.toDomain(): QuestRecordedDetailModel {
    return QuestRecordedDetailModel(
        question = this.question,
        questAnswer = this.answer,
        imageUrl = this.imageUrl,
        stepNumber = this.stepNumber,
        questNumber = this.questNumber,
        createdAt = this.createdAt.toString(),
        questEmotionState = this.questEmotionState,
        emotionDescription = this.emotionDescription
    )
}
