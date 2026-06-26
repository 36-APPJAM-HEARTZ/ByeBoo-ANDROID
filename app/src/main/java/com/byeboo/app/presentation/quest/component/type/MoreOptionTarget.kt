package com.byeboo.app.presentation.quest.component.type

sealed interface MoreOptionTarget {
    val id: Long
    val writerId: Long

    data class Answer(
        override val id: Long,
        override val writerId: Long,
    ) : MoreOptionTarget

    data class Comment(
        override val id: Long,
        override val writerId: Long,
    ) : MoreOptionTarget

    data class Reply(
        override val id: Long,
        override val writerId: Long,
    ) : MoreOptionTarget
}
