package com.byeboo.app.presentation.home.homeamulet

import com.byeboo.app.R

data class HomeAmuletState(
    val journey: AmuletType = AmuletType.RECORDING,
    val journeyDescription: String = "",
    val canFlip: Boolean = false,
)

sealed interface HomeAmuletSideEffect {
    data object NavigateToHomeOnboarding : HomeAmuletSideEffect

    data class ShowSnackBar(
        val message: String,
    ) : HomeAmuletSideEffect
}

enum class AmuletType(
    val journeyName: String,
    val frontImg: Int,
    val backImg: Int,
) {
    REUNION(
        journeyName = "재회 준비",
        frontImg = R.drawable.img_reunion_amulet_front,
        backImg = R.drawable.img_reunion_amulet_back,
    ),
    RECORDING(
    journeyName = "이별 극복",
    frontImg = R.drawable.img_recording_amulet_front,
    backImg = R.drawable.img_recording_amulet_back,
    );

    companion object {
        fun from(journeyName: String): AmuletType =
            entries.find { it.journeyName == journeyName } ?: RECORDING
    }
}
