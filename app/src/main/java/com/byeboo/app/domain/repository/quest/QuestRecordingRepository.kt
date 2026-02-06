package com.byeboo.app.domain.repository.quest

import com.byeboo.app.domain.model.quest.QuestRecordingEditModel
import com.byeboo.app.domain.model.quest.QuestRecordingModel

interface QuestRecordingRepository {
    suspend fun postRecording(
        questId: Long,
        request: QuestRecordingModel,
    ): Result<Unit>

    suspend fun updateRecording(
        questId: Long,
        request: QuestRecordingEditModel,
    ): Result<Unit>
}
