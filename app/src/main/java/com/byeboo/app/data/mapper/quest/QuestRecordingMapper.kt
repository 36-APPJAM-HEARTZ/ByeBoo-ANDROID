package com.byeboo.app.data.mapper.quest

import com.byeboo.app.data.dto.request.quest.QuestRecordingEditRequestDto
import com.byeboo.app.data.dto.request.quest.QuestRecordingRequestDto
import com.byeboo.app.domain.model.quest.QuestRecordingEditModel
import com.byeboo.app.domain.model.quest.QuestRecordingModel

fun QuestRecordingModel.toData(): QuestRecordingRequestDto =
    QuestRecordingRequestDto(
        answer = this.answer,
        questEmotionState = this.questEmotionState
    )

fun QuestRecordingEditModel.toData(): QuestRecordingEditRequestDto =
    QuestRecordingEditRequestDto(
        answer = this.answer,
    )
