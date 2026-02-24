package com.byeboo.app.presentation.quest.review.common

import com.byeboo.app.presentation.quest.model.CommonAnswerModel


data class CommonAnswerState(
    val answer: CommonAnswerModel = CommonAnswerModel(
        answerId = -1L,
        writer = "사용자닉네",
        profileIconRes = -1,
        displayTime = "2026.01.30",
        content = "헤어진 첫날 밤이었어요. 혼자 집에 있는데 갑자기 모든 게 현실로 다가왔고, 이제 정말 끝났다는 걸 깨달았을 때... 그때가 제일 힘들었던 것 같아요."
    )
)