package com.byeboo.app.domain.model.quest

object QuestContentLengthValidator {
    fun validate(
        isFocused: Boolean,
        text: String,
        maxLength: Int = 500
    ): QuestWritingState =
        when {
            text.isBlank() -> QuestWritingState.Empty
            isFocused -> QuestWritingState.Writing
            text.length > maxLength -> QuestWritingState.OverLimit
            else -> QuestWritingState.Ready
        }

    fun validButton(
        text: String,
        maxLength: Int = 500
    ): Boolean = text.length >= 10 && text.length <= maxLength
}
