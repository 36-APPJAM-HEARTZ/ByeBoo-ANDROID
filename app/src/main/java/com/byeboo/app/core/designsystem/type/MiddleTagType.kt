package com.byeboo.app.core.designsystem.type

import androidx.annotation.StringRes
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.byeboo.app.R

enum class MiddleTagType(
    @StringRes val titleResId: Int,
    val horizontalPadding: Dp = 18.dp,
    val verticalPadding: Dp = 4.dp,
    val roundedCorner: Dp = 12.dp,
    val dynamicContent: Boolean = false,
) {
    QUEST_START_DAY(
        titleResId = R.string.type_quest_start_day,
        dynamicContent = true,
    ),

    QUEST_TIP(
        titleResId = R.string.type_quest_tip,
        dynamicContent = false,
    ),

    QUEST_ESSENTIAL(
        titleResId = R.string.type_quest_essential,
        dynamicContent = false,
    ),

    QUEST_OPTIONAL(
        titleResId = R.string.type_quest_optional,
        dynamicContent = false,
    ),

    QUEST_PERIOD(
        titleResId = R.string.type_quest_period,
        dynamicContent = true,
    ),
}
