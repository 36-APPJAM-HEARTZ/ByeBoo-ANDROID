package com.byeboo.app.domain.model.quest

import java.time.LocalDateTime

data class CommonQuestAnswer(
    val answerId: Long,
    val profileIcon: String,
    val writer: String,
    val writtenAt: LocalDateTime,
    val content: String,
)
