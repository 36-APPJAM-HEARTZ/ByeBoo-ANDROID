package com.byeboo.app.data.mapper.quest

import com.byeboo.app.data.dto.request.quest.QuestBehaviorEditRequestDto
import com.byeboo.app.data.dto.request.quest.QuestBehaviorRequestDto
import com.byeboo.app.data.dto.request.quest.QuestSignedUrlRequestDto
import com.byeboo.app.domain.model.quest.BehaviorAnswerRequestModel
import com.byeboo.app.domain.model.quest.QuestBehaviorEditModel
import com.byeboo.app.domain.model.quest.SignedUrlRequestModel

fun SignedUrlRequestModel.toData(): QuestSignedUrlRequestDto =
    QuestSignedUrlRequestDto(
        contentType = this.contentType,
        imageKey = this.imageKey,
    )

fun BehaviorAnswerRequestModel.toData(): QuestBehaviorRequestDto =
    QuestBehaviorRequestDto(
        answer = this.answer,
        questEmotionState = this.questEmotionState,
        imageKey = imageKey,
    )

fun QuestBehaviorEditModel.toData(): QuestBehaviorEditRequestDto =
    QuestBehaviorEditRequestDto(
        answer = this.answer,
        imageKey = this.imageKey,
    )
