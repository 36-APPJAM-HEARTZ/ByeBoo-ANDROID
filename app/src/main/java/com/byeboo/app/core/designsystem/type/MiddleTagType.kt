package com.byeboo.app.core.designsystem.type

import androidx.annotation.StringRes
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.byeboo.app.R

enum class MiddleTagType(
    @StringRes val titleResId: Int,
    val backgroundColor: TagColorType,
    val textColor: TagColorType,
    val horizontalPadding: Dp = 18.dp,
    val verticalPadding: Dp = 4.dp,
    val roundedCorner: Dp = 12.dp,
    val dynamicContent: Boolean = false

) {
    QUEST_START_DAY(
        titleResId = R.string.type_quest_start_day,
        backgroundColor = TagColorType.WHITE_ALPHA_10,
        textColor = TagColorType.GRAY_300,
        dynamicContent = true
    ),

    QUEST_TIP(
        titleResId = R.string.type_quest_tip,
        backgroundColor = TagColorType.PRIMARY_300,
        textColor = TagColorType.PRIMARY_50,
        dynamicContent = false
    ),

    QUEST_ESSENTIAL(
        titleResId = R.string.type_quest_essential,
        backgroundColor = TagColorType.SECONDARY_ALPHA_30,
        textColor = TagColorType.SECONDARY_300,
        dynamicContent = false
    ),

    QUEST_OPTIONAL(
        titleResId = R.string.type_quest_optional,
        backgroundColor = TagColorType.WHITE_ALPHA_10,
        textColor = TagColorType.GRAY_300,
        dynamicContent = false
    ),

    QUEST_PERIOD(
        titleResId = R.string.type_quest_period,
        backgroundColor = TagColorType.WHITE_ALPHA_10,
        textColor = TagColorType.GRAY_300,
        dynamicContent = true
    )
}

enum class TagColorType {
    WHITE_ALPHA_10,
    SECONDARY_ALPHA_30,
    GRAY_300,
    SECONDARY_300,
    PRIMARY_50,
    PRIMARY_300
}
