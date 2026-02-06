package com.byeboo.app.data.mapper.quest

import com.byeboo.app.data.dto.response.quest.QuestDetailResponseDto
import com.byeboo.app.domain.model.auth.QuestStyle
import com.byeboo.app.domain.model.quest.QuestDetailModel

fun QuestDetailResponseDto.toDomain(): QuestDetailModel =
    QuestDetailModel(
        step = this.step,
        stepNumber = this.stepNumber,
        questNumber = this.questNumber,
        questStyle = QuestStyle.valueOf(this.questStyle),
        question = this.question,
    )
