package com.byeboo.app.data.mapper.quest

import com.byeboo.app.core.designsystem.type.EmotionChipType

fun EmotionChipType.toData(): String =
    when (this) {
        EmotionChipType.EMOTION_NEUTRAL -> "NEUTRAL"
        EmotionChipType.EMOTION_SADNESS -> "SAD"
        EmotionChipType.EMOTION_RELIEF -> "RELIEVED"
        EmotionChipType.EMOTION_SELF_AWARE -> "SELF_UNDERSTANDING"
    }
