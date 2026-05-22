package com.byeboo.app.presentation.notification
import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class NotificationModel(
    val notificationId: Long,
    val notificationType: String,
    val title: String,
    val content: String,
    val isRead: Boolean,
    val createdAt: String,
    val landingLink: String
)

val dummyNotifications = persistentListOf(
    NotificationModel(
        notificationId = 1L,
        notificationType  = "QUEST_OPEN", // 아이콘 분기용 타입 (추가하신 스펙 기준)
        title = "오늘의 퀘스트 오픈 🌱",
        content = "새로운 감정 기록 퀘스트가 도착했어요. 내 마음을 들여다볼 시간이에요!",
        createdAt = "방금 전",
        isRead = false,
        landingLink = ""
    ),
    NotificationModel(
        notificationId = 2L,
        notificationType = "QUEST_EMPATHY",
        title = "내가 작성한 글에 보리보리쌀님이 공감을 남겼어요",
        content = "정말 공감되는 글이에요. 오늘 하루도 수고 많으셨습니다!",
        createdAt = "1시간 전",
        isRead = false,
        landingLink = "byeboo://quest/detail/102"
    ),
    NotificationModel(
        notificationId = 3L,
        notificationType = "QUEST_REPLY",
        title = "내가 작성한 글에 익명의 유저님이 답변을 남겼어요",
        content = "저도 비슷한 경험이 있어요. 다들 그렇게 이겨내나 봐요. 응원합니다!",
        createdAt = "3시간 전",
        isRead = true,
        landingLink = "byeboo://quest/detail/102"
    ),
    NotificationModel(
        notificationId = 4L,
        notificationType = "NOTICE",
        title = "ByeBoo v1.2 업데이트 안내 📣",
        content = "더욱 따뜻해진 바이부의 새로운 기능들을 만나보세요.",
        createdAt = "어제",
        isRead = true,
        landingLink = "byeboo://notice/20"
    ),
    NotificationModel(
        notificationId = 5L,
        notificationType = "QUEST_EMPATHY",
        title = "내가 작성한 글에 감성고양이님이 공감을 남겼어요",
        content = "당신의 하루를 응원합니다!",
        createdAt = "2026.05.20",
        isRead = true,
        landingLink = "byeboo://quest/detail/88"
    )
)
