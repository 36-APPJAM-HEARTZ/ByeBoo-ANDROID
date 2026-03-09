package com.byeboo.app.data.mapper.quest

import com.byeboo.app.data.dto.request.quest.QuestCommonEditRequestDto
import com.byeboo.app.data.dto.request.quest.QuestCommonRequestDto
import com.byeboo.app.data.dto.response.quest.CommonQuestResponseDto
import com.byeboo.app.data.dto.response.quest.QuestAnswerDto
import com.byeboo.app.data.dto.response.quest.QuestCommonAnswerDetailResponseDto
import com.byeboo.app.data.dto.response.quest.QuestMyCommonAnswerResponseDto
import com.byeboo.app.domain.model.quest.CommonQuestAnswer
import com.byeboo.app.domain.model.quest.CommonQuestModel
import com.byeboo.app.domain.model.quest.QuestAnswerDetailModel
import com.byeboo.app.domain.model.quest.QuestAnswerModel
import com.byeboo.app.domain.model.quest.QuestCommonAnswerEditModel
import com.byeboo.app.domain.model.quest.QuestCommonAnswerRequestModel
import com.byeboo.app.domain.model.quest.QuestCommonMyAnswerModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun QuestCommonAnswerRequestModel.toData(): QuestCommonRequestDto =
    QuestCommonRequestDto(
        answer = this.answer,
    )

fun QuestMyCommonAnswerResponseDto.toDomain(): QuestCommonMyAnswerModel =
    QuestCommonMyAnswerModel(
        hasNext = this.hasNext,
        nextCursor = this.nextCursor,
        answers =
            this.answers.map {
                QuestAnswerModel(
                    question = it.question,
                    answerId = it.answerId,
                    writtenAt = it.writtenAt.split("T")[0],
                    content = it.content,
                )
            },
    )

fun QuestCommonAnswerEditModel.toData(): QuestCommonEditRequestDto =
    QuestCommonEditRequestDto(
        answer = this.answer,
    )

fun CommonQuestResponseDto.toDomain(): CommonQuestModel =
    CommonQuestModel(
        question = this.question,
        answerCount = this.answerCount,
        answers = this.answers.map { it.toDomain() },
        isAnswered = this.isAnswered,
        hasNext = this.hasNext,
        nextCursor = this.nextCursor,
        questId = this.questId,
    )

fun QuestAnswerDto.toDomain(): CommonQuestAnswer =
    CommonQuestAnswer(
        answerId = this.answerId,
        profileIcon = this.profileIcon,
        writer = this.writer,
        writtenAt = LocalDateTime.parse(this.writtenAt, DateTimeFormatter.ISO_LOCAL_DATE_TIME),
        content = this.content,
    )

fun QuestCommonAnswerDetailResponseDto.toDomain(): QuestAnswerDetailModel =
    QuestAnswerDetailModel(
        question = this.question,
        writer = this.writer,
        writerId = this.writerId,
        profileIcon = this.profileIcon,
        content = this.content,
        writtenAt = LocalDate.parse(this.writtenAt, DateTimeFormatter.ISO_LOCAL_DATE).atStartOfDay(),
    )
