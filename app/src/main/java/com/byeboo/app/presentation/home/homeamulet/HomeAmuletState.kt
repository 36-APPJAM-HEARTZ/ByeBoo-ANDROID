package com.byeboo.app.presentation.home.homeamulet

import com.byeboo.app.R
import com.byeboo.app.core.designsystem.type.CustomSnackBarType

data class HomeAmuletState(
    val journey: AmuletType = AmuletType.EMOTION_FACE,
    val journeyDescription: String = "",
    val canFlip: Boolean = false,
)

sealed interface HomeAmuletSideEffect {
    data object NavigateToHomeOnboarding : HomeAmuletSideEffect

    data class ShowSnackBar(
        val snackBarType: CustomSnackBarType,
    ) : HomeAmuletSideEffect
}

enum class AmuletType(
    val journeyName: String,
    val frontImg: Int,
    val backImg: Int,
) {
    EMOTION_FACE(
        "감정 직면",
        R.drawable.img_recording_amulet_front,
        R.drawable.img_recording_amulet_back,
    ),
    EMOTION_ORGANIZE(
        "감정 정리",
        R.drawable.img_active_amulet_front,
        R.drawable.img_active_amulet_back,
    ),
    ;

    companion object {
        fun from(journeyName: String): AmuletType = entries.find { it.journeyName == journeyName } ?: EMOTION_FACE
    }
}
