package com.byeboo.app.data.mapper.quest

import com.byeboo.app.core.designsystem.type.LargeTagType

fun LargeTagType.toData(): String =
    when (this) {
        LargeTagType.EMOTION_NEUTRAL -> "NEUTRAL"
        LargeTagType.EMOTION_SADNESS -> "SAD"
        LargeTagType.EMOTION_RELIEF -> "RELIEVED"
        LargeTagType.EMOTION_SELF_AWARE -> "SELF_UNDERSTANDING"
    }
