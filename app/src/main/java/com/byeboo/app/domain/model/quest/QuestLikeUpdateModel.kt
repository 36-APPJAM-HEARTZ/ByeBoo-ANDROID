package com.byeboo.app.domain.model.quest

data class QuestLikeUpdateModel(
    val answerId: Long,
    val heartCount: Int,
    val isLiked: Boolean,
)
