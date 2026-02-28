package com.byeboo.app.presentation.quest.component.type

import androidx.annotation.DrawableRes
import com.byeboo.app.R


sealed interface PostOption{
    val optionIcon: Int
    val optionTitle: String
}

enum class MyPostOption(
    @DrawableRes override val optionIcon: Int,
    override val optionTitle: String,
) : PostOption {
    EDIT(R.drawable.ic_edit, "수정하기"),
    DELETE(R.drawable.ic_trash, "삭제하기")
}

enum class OtherPostOption(
    @DrawableRes override val optionIcon: Int,
    override val optionTitle: String,
) : PostOption {
    BLOCK(R.drawable.ic_block, "사용자 차단하기"),
    REPORT(R.drawable.ic_report, "게시글 신고하기")
}

