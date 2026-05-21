package com.byeboo.app.presentation.notification

import androidx.annotation.DrawableRes
import com.byeboo.app.R

sealed class NotificationType(
    @param:DrawableRes val icon: Int,
    val title: String,
    val content: String,
) {
    data class QuestOpen(
        val questId: Long,
    ): NotificationType(icon = R.drawable.ic_notification_quest, title = "오늘의 퀘스트 오픈🌱", content = "${questId}번째 퀘스트가 오픈됐어요, 시작해볼까요?")

    data class QuestEmpathy(
        val questId: Long,
        val userName: String,
    ): NotificationType(icon = R.drawable.ic_notification_reaction, title = "공통여정 답변에 공감이 달렸어요❤️",content="내가 작성한 글에 ${userName}님이 공감을 남겼어요️")

    data class QuestReply(
        val questId: Long,
        val userName: String,
    ): NotificationType(icon = R.drawable.ic_notification_reaction, title = "공통여정에 공감이 달렸어요💬", content = "내가 작성한 글에 ${userName}님이 답변을 남겼어요")
}

