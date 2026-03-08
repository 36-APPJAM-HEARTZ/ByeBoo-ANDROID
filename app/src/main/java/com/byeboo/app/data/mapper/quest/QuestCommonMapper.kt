package com.byeboo.app.data.mapper.quest

import com.byeboo.app.data.dto.request.quest.QuestCommonEditRequestDto
import com.byeboo.app.data.dto.request.quest.QuestCommonRequestDto
import com.byeboo.app.data.dto.response.quest.QuestMyCommonAnswerResponseDto
import com.byeboo.app.domain.model.quest.QuestAnswerModel
import com.byeboo.app.domain.model.quest.QuestCommonAnswerEditModel
import com.byeboo.app.domain.model.quest.QuestCommonAnswerRequestModel
import com.byeboo.app.domain.model.quest.QuestCommonMyAnswerModel

fun QuestCommonAnswerRequestModel.toData(): QuestCommonRequestDto =
    QuestCommonRequestDto(
        answer = this.answer
    )

fun QuestMyCommonAnswerResponseDto.toDomain(): QuestCommonMyAnswerModel =
    QuestCommonMyAnswerModel(
        hasNext = this.hasNext,
        nextCursor = this.nextCursor,
        answers = this.answers.map {
            QuestAnswerModel(
                question = it.question,
                answerId = it.answerId,
                writtenAt = it.writtenAt.split("T")[0],
                content = it.content
            )
        }
    )

fun QuestCommonAnswerEditModel.toData(): QuestCommonEditRequestDto =
    QuestCommonEditRequestDto(
        answer = this.answer
    )