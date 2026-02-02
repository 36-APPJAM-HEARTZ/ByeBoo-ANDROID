package com.byeboo.app.data.datasourceimpl.remote.quest

import com.byeboo.app.data.datasource.remote.quest.QuestRecordingDataSource
import com.byeboo.app.data.dto.base.NullableBaseResponse
import com.byeboo.app.data.dto.request.quest.QuestRecordingEditRequestDto
import com.byeboo.app.data.dto.request.quest.QuestRecordingRequestDto
import com.byeboo.app.data.service.quest.QuestRecordingService
import javax.inject.Inject

class QuestRecordingDataSourceImpl
    @Inject
    constructor(
        private val questRecordingService: QuestRecordingService,
    ) : QuestRecordingDataSource {
        override suspend fun postQuestRecording(
            questId: Long,
            request: QuestRecordingRequestDto,
        ): NullableBaseResponse<Unit> =
            questRecordingService.postRecording(
                questId = questId,
                request = request,
            )

        override suspend fun updateQuestRecording(
            questId: Long,
            request: QuestRecordingEditRequestDto,
        ): NullableBaseResponse<Unit> =
            questRecordingService.patchRecording(
                questId = questId,
                request = request,
            )
    }
