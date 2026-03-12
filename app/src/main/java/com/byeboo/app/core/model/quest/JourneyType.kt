package com.byeboo.app.core.model.quest

import androidx.annotation.DrawableRes
import com.byeboo.app.R

enum class JourneyType(
    val journeyName: String,
    val journeyType: String,
    @DrawableRes val frontImg: Int = R.drawable.img_recording_amulet_front,
    @DrawableRes val backImg: Int = R.drawable.img_recording_amulet_back,
) {
    RECORDING(
        journeyName = "이별 극복",
        journeyType = "FACE_EMOTION",
        frontImg = R.drawable.img_recording_amulet_front,
        backImg = R.drawable.img_recording_amulet_back,
    ),

    ACTIVE(
        journeyName = "감정 정리",
        journeyType = "PROCESS_EMOTION",
    ),

    REUNION(
        journeyName = "재회 준비",
        journeyType = "PREPARE_REUNION",
        frontImg = R.drawable.img_reunion_amulet_front,
        backImg = R.drawable.img_reunion_amulet_back,
    ),

    UNKNOWN(
        journeyName = "알 수 없음",
        journeyType = "UNKNOWN",
    ),
    ;

    companion object {
        fun fromJourneyName(journeyName: String): JourneyType = JourneyType.entries.find { it.journeyName == journeyName } ?: UNKNOWN

        fun fromStyle(style: String): JourneyType = JourneyType.entries.find { it.name == style } ?: UNKNOWN
    }
}
