package com.byeboo.app.presentation.tutorial

import androidx.annotation.DrawableRes
import com.byeboo.app.R

enum class TutorialContent(
    @DrawableRes val image: Int,
    val content: String
) {
    FIRST_CONTENT(
        image = R.drawable.img_tutorial_1,
        content = "저는 당신이 털어놓은 감정을 담는 보따리,\n보리라고 해요."
    ),
    SECOND_CONTENT(
        image = R.drawable.img_tutorial_2,
        content = "이별 후 걸림돌 같은 감정들을 털어놔 주시면\n제 안에서 감정돌이 되어 쌓여요."
    ),
    THIRD_CONTENT(
        image = R.drawable.img_tutorial_3,
        content = "제가 모은 감정돌들은\n당신이 이별을 극복한 후 새 출발을\n할 수 있도록 돕는 디딤돌이 될 거예요."
    ),
    FOURTH_CONTENT(
        image = R.drawable.img_tutorial_4,
        content = "제가 모아둔 감정돌을 디디돌 삼아\n한 걸음 한 걸음 미래로 나아가주세요."
    ),
    FIFTH_CONTENT(
        image = R.drawable.img_tutorial_5,
        content = "자, 이제 시간이 됐어요.\n\n당신에게 꼭 맞는 이별 극복 여정에 따라\n" +
                "퀘스트를 하나하나씩 진행하면서\n감정을 정리하고 극복해 보아요.\n\n" +
                "저 보리가 항상 함께할게요."
    )
}

sealed interface TutorialSideEffect {
    data object NavigateToUp: TutorialSideEffect
}