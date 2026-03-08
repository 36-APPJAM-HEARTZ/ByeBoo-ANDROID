package com.byeboo.app.data.mapper.quest

import com.byeboo.app.data.dto.response.quest.CommonQuestResponseDto
import com.byeboo.app.data.dto.response.quest.QuestAnswerDto
import com.byeboo.app.domain.model.quest.CommonQuestAnswer
import com.byeboo.app.domain.model.quest.CommonQuestModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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
