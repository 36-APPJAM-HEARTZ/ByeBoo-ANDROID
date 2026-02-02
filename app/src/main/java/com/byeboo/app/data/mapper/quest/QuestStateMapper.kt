package com.byeboo.app.data.mapper.quest

import com.byeboo.app.data.dto.response.quest.QuestCountResponseDto
import com.byeboo.app.data.dto.response.quest.QuestDialogueResponseDto
import com.byeboo.app.domain.model.quest.QuestDialogue
import com.byeboo.app.domain.model.quest.QuestStateModel

fun QuestCountResponseDto.toDomain(): QuestStateModel =
    QuestStateModel(
        todayComplete = this.todayComplete,
        userCurrentStatus = this.userCurrentStatus,
        count = this.count.toLong()
    )

fun QuestDialogueResponseDto.toDomain(): QuestDialogue =
    QuestDialogue(
        dialogue = this.dialogue
    )
