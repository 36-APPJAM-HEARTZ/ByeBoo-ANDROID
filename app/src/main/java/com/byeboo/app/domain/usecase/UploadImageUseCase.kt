package com.byeboo.app.domain.usecase

import com.byeboo.app.domain.model.quest.BehaviorAnswerRequestModel
import com.byeboo.app.domain.model.quest.SignedUrlRequestModel
import com.byeboo.app.domain.repository.quest.QuestBehaviorAnswerRepository
import javax.inject.Inject

class UploadImageUseCase @Inject constructor(
    private val questBehaviorAnswerRepository: QuestBehaviorAnswerRepository
) {
    suspend operator fun invoke(
        imageBytes: ByteArray,
        contentType: String,
        imageKey: String,
        questId: Long,
        answer: String,
        emotion: String
    ): Result<Unit> = runCatching {
        val signedUrl = questBehaviorAnswerRepository.requestQuestSignedUrl(
            SignedUrlRequestModel(contentType, imageKey)
        ).getOrThrow()

        questBehaviorAnswerRepository.uploadImageToSignedUrl(signedUrl, imageBytes, contentType)

        val request = BehaviorAnswerRequestModel(
            answer = answer,
            questEmotionState = emotion,
            imageKey = imageKey
        )

        questBehaviorAnswerRepository.uploadQuestBehaviorAnswer(questId, request)
    }
}
