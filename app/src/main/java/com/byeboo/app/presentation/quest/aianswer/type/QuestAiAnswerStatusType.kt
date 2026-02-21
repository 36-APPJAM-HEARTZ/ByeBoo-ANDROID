package com.byeboo.app.presentation.quest.aianswer.type

import androidx.annotation.DrawableRes
import com.byeboo.app.R

enum class QuestAiAnswerStatusType(
    @DrawableRes val statusImage: Int,
    val statusContent: String,
) {
    LOADING(
        statusImage = R.drawable.img_ai_bori_loading,
        statusContent = "보리가 열심히 답변을 작성하고 있어요!",
    ),

    FAIL(
        statusImage = R.drawable.img_ai_bori_loading_fail,
        statusContent = "답변 생성을 실패했어요.\n잠시 뒤에 다시 시도해 주세요.",
    ),
}
