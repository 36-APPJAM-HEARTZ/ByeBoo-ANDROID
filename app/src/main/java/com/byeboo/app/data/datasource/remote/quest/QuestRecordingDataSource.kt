package com.byeboo.app.data.datasource.remote.quest

import com.byeboo.app.data.dto.base.NullableBaseResponse
import com.byeboo.app.data.dto.request.quest.QuestRecordingEditRequestDto
import com.byeboo.app.data.dto.request.quest.QuestRecordingRequestDto

interface QuestRecordingDataSource {
    suspend fun postQuestRecording(questId: Long, request: QuestRecordingRequestDto): NullableBaseResponse<Unit>
    suspend fun updateQuestRecording(questId: Long, request: QuestRecordingEditRequestDto): NullableBaseResponse<Unit>
}
