package com.byeboo.app.domain.repository.quest

import com.byeboo.app.domain.model.quest.BehaviorAnswerRequestModel
import com.byeboo.app.domain.model.quest.SignedUrlRequestModel

interface QuestBehaviorAnswerRepository {
    suspend fun requestQuestSignedUrl(request: SignedUrlRequestModel): Result<String>
    suspend fun uploadImageToSignedUrl(signUrl: String, imageBytes: ByteArray, contentType: String): Result<Unit>
    suspend fun uploadQuestBehaviorAnswer(questId: Long, request: BehaviorAnswerRequestModel): Result<Unit>
}
