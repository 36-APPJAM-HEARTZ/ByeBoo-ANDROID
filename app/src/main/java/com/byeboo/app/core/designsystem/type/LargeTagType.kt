package com.byeboo.app.core.designsystem.type

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.byeboo.app.R

enum class LargeTagType(
    @DrawableRes val titleIcon: Int,
    @StringRes val titleResId: Int,
    val title: String,
    val verticalPadding: Dp = 3.dp,
    val roundedCorner: Dp = 12.dp
) {
    EMOTION_NEUTRAL(
        titleIcon = R.drawable.img_natural,
        titleResId = R.string.type_emotion_neutral,
        title = "NEUTRAL"
    ),

    EMOTION_SELF_AWARE(
        titleIcon = R.drawable.img_self_aware,
        titleResId = R.string.type_emotion_self_aware,
        title = "SELF_UNDERSTANDING"
    ),

    EMOTION_SADNESS(
        titleIcon = R.drawable.img_sadness,
        titleResId = R.string.type_emotion_sadness,
        title = "SAD"
    ),

    EMOTION_RELIEF(
        titleIcon = R.drawable.img_relief,
        titleResId = R.string.type_emotion_relief,
        title = "RELIEVED"
    );

    companion object {
        fun fromKorean(value: String): LargeTagType {
            return when (value) {
                "슬픔" -> EMOTION_SADNESS
                "후련함" -> EMOTION_RELIEF
                "자기 이해" -> EMOTION_SELF_AWARE
                else -> EMOTION_NEUTRAL
            }
        }
    }
}
