package com.byeboo.app.data.mapper.quest

import com.byeboo.app.data.dto.response.quest.QuestAiAnswerResponseDto
import com.byeboo.app.domain.model.quest.QuestAiAnswerModel

fun QuestAiAnswerResponseDto.toDomain(): QuestAiAnswerModel =
    QuestAiAnswerModel(
        aiAnswer = this.aiAnswer,
    )
