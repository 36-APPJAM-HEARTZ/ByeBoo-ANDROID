package com.byeboo.app.presentation.quest.component.type

import androidx.annotation.DrawableRes
import com.byeboo.app.R

enum class OptionType(
    @DrawableRes val optionIcon: Int,
    val optionTitle: String,
) {
    BLOCK(
        optionIcon = R.drawable.ic_block,
        optionTitle = "사용자 차단하기",
    ),

    REPORT(
        optionIcon = R.drawable.ic_report,
        optionTitle = "게시글 신고하기",
    ),

    EDIT(
        optionIcon = R.drawable.ic_edit,
        optionTitle = "수정하기",
    ),

    DELETE(
        optionIcon = R.drawable.ic_trash,
        optionTitle = "삭제하기",
    ),
}
