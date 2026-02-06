package com.byeboo.app.data.repositoryimpl.quest.behavior

import com.byeboo.app.data.datasource.remote.quest.QuestBehaviorDataSource
import com.byeboo.app.data.mapper.quest.toData
import com.byeboo.app.domain.model.quest.BehaviorAnswerRequestModel
import com.byeboo.app.domain.model.quest.QuestBehaviorEditModel
import com.byeboo.app.domain.model.quest.SignedUrlRequestModel
import com.byeboo.app.domain.repository.quest.QuestBehaviorRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class QuestBehaviorRepositoryImpl
    @Inject
    constructor(
        private val questBehaviorAnswerDataSource: QuestBehaviorDataSource,
    ) : QuestBehaviorRepository {
        override suspend fun requestQuestSignedUrl(request: SignedUrlRequestModel): Result<String> =
            runCatching {
                val response = questBehaviorAnswerDataSource.requestQuestSignedUrl(request.toData())
                response.data.signedUrl
            }

        override suspend fun uploadImageToSignedUrl(
            signUrl: String,
            imageBytes: ByteArray,
            contentType: String,
        ): Result<Unit> =
            runCatching {
                val body = imageBytes.toRequestBody(contentType.toMediaTypeOrNull())
                questBehaviorAnswerDataSource.uploadImageToSignedUrl(signUrl, body)
            }

        override suspend fun uploadQuestBehaviorAnswer(
            questId: Long,
            request: BehaviorAnswerRequestModel,
        ): Result<Unit> =
            runCatching {
                questBehaviorAnswerDataSource.uploadQuestBehaviorAnswer(questId, request.toData())
            }

        override suspend fun updateQuestBehavior(
            questId: Long,
            request: QuestBehaviorEditModel,
        ): Result<Unit> =
            runCatching {
                val response =
                    questBehaviorAnswerDataSource.updateQuestBehavior(
                        questId = questId,
                        request = request.toData(),
                    )

                if (!response.success) {
                    throw IllegalStateException("update quest behavior failed")
                }
            }
    }
