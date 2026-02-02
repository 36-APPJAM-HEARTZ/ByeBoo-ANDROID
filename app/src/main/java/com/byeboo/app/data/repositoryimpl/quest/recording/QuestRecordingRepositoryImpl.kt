package com.byeboo.app.data.repositoryimpl.quest.recording

import com.byeboo.app.data.datasource.remote.quest.QuestRecordingDataSource
import com.byeboo.app.data.mapper.quest.toData
import com.byeboo.app.domain.model.quest.QuestRecordingEditModel
import com.byeboo.app.domain.model.quest.QuestRecordingModel
import com.byeboo.app.domain.repository.quest.QuestRecordingRepository
import javax.inject.Inject

class QuestRecordingRepositoryImpl
@Inject
constructor(
    private val questRecordingDataSource: QuestRecordingDataSource
) : QuestRecordingRepository {
    override suspend fun postRecording(
        questId: Long,
        request: QuestRecordingModel
    ): Result<Unit> =
        runCatching {
            val response = questRecordingDataSource.postQuestRecording(
                questId,
                request.toData()
            )
            if (!response.success) {
                throw IllegalStateException("postRecording failed")
            }
            Unit
        }

    override suspend fun updateRecording(
        questId: Long,
        request: QuestRecordingEditModel
    ): Result<Unit> =
        runCatching {
            val response = questRecordingDataSource.updateQuestRecording(
                questId,
                request.toData()
            )
            if (!response.success) {
                throw IllegalStateException("updateRecording failed")
            }
            Unit
        }
}
