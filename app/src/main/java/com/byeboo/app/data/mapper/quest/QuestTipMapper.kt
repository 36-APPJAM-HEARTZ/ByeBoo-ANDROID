package com.byeboo.app.data.mapper.quest

import com.byeboo.app.data.dto.response.quest.QuestTipResponseDto
import com.byeboo.app.data.dto.response.quest.Tip
import com.byeboo.app.domain.model.quest.QuestTip
import com.byeboo.app.domain.model.quest.QuestTips

fun QuestTipResponseDto.toDomain(): QuestTip =
    QuestTip(
        question = this.question,
        step = this.step,
        stepNumber = this.stepNumber,
        questNumber = this.questNumber,
        tips = this.tips.map { it: Tip -> it.toDomain() },
    )

fun Tip.toDomain(): QuestTips =
    QuestTips(
        tipStep = this.tipStep,
        tipAnswer = this.tipAnswer,
    )
