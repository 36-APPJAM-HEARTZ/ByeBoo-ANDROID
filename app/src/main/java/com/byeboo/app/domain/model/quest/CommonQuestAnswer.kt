package com.byeboo.app.domain.model.quest

import java.time.LocalDateTime

data class CommonQuestAnswer(
    val answerId: Long,
    val writer: String,      
    val writtenAt: LocalDateTime, 
    val content: String,     
    val isMine: Boolean = false 
)