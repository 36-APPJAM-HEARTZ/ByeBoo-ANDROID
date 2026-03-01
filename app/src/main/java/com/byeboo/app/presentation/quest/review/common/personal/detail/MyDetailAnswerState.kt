package com.byeboo.app.presentation.quest.review.common.personal.detail

import androidx.compose.runtime.Immutable
import com.byeboo.app.presentation.quest.model.MyAnswerModel

@Immutable
data class MyDetailAnswerState(
    val answer: MyAnswerModel =
        MyAnswerModel(
            answerId = 0L,
            question = "",
            writtenAt = "",
            content = "",
        ),
    val showBottomSheet: Boolean = false,
)
