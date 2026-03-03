package com.byeboo.app.data.mapper.quest

import com.byeboo.app.data.dto.request.quest.QuestCommonRequestDto
import com.byeboo.app.domain.model.quest.QuestCommonAnswerRequestModel

fun QuestCommonAnswerRequestModel.toData(): QuestCommonRequestDto =
    QuestCommonRequestDto(
        answer = this.answer
    )