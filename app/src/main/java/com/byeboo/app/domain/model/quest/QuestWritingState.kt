package com.byeboo.app.domain.model.quest

sealed class QuestWritingState {
    object Empty : QuestWritingState()

    object Writing : QuestWritingState()

    object OverLimit : QuestWritingState()

    object Ready : QuestWritingState()
}
