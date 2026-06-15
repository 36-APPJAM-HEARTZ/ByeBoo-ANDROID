package com.byeboo.app.presentation.notification
import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class NotificationModel(
    val notificationId: Long,
    val notificationType: NotificationType,
    val title: String,
    val content: String,
    val isRead: Boolean,
    val createdAt: String,
    val landingLink: String,
)

val dummyNotifications =
    persistentListOf(
        NotificationModel(
            notificationId = 1L,
            notificationType = NotificationType.COMMENT,
            title = "오늘의 퀘스트 오픈 🌱",
            content = "24번째 퀘스트가 오픈됐어요, 시작해볼까요?",
            createdAt = "방금 전",
            isRead = false,
            landingLink = "",
        ),
        NotificationModel(
            notificationId = 2L,
            notificationType = NotificationType.LIKE,
            title = "공통여정에 답변에 공감이 달렸어요",
            content = "내가 작성한 글에 보리보리쌀님이 공감을 남겼어요",
            createdAt = "1시간 전",
            isRead = false,
            landingLink = "",
        ),
        NotificationModel(
            notificationId = 3L,
            notificationType = NotificationType.COMMENT,
            title = "공통여정에 댓글이 달렸어요",
            content = "내가 작성한 글에 보리보리쌀님이 댓글을 남겼어요",
            createdAt = "3시간 전",
            isRead = true,
            landingLink = "",
        ),
        NotificationModel(
            notificationId = 4L,
            notificationType = NotificationType.QUEST_OPEN,
            title = "오늘의 퀘스트 오픈 🌱",
            content = "25번째 퀘스트가 오픈됐어요, 시작해볼까요?",
            createdAt = "1시간 전",
            isRead = false,
            landingLink = "",
        ),
        NotificationModel(
            notificationId = 5L,
            notificationType = NotificationType.QUEST_OPEN,
            title = "오늘의 퀘스트 오픈 🌱",
            content = "25번째 퀘스트가 오픈됐어요, 시작해볼까요?",
            createdAt = "1시간 전",
            isRead = false,
            landingLink = "",
        ),
        NotificationModel(
            notificationId = 6L,
            notificationType = NotificationType.QUEST_OPEN,
            title = "오늘의 퀘스트 오픈 🌱",
            content = "25번째 퀘스트가 오픈됐어요, 시작해볼까요?",
            createdAt = "1시간 전",
            isRead = false,
            landingLink = "",
        ),
    )
