package com.byeboo.app.domain.usecase

import com.byeboo.app.domain.model.quest.BehaviorAnswerRequestModel
import com.byeboo.app.domain.model.quest.QuestBehaviorEditModel
import com.byeboo.app.domain.model.quest.SignedUrlRequestModel
import com.byeboo.app.domain.repository.quest.QuestBehaviorRepository
import javax.inject.Inject

class UploadImageUseCase @Inject constructor(
    private val questBehaviorRepository: QuestBehaviorRepository
) {
    suspend operator fun invoke(
        imageBytes: ByteArray,
        contentType: String,
        imageKey: String,
        questId: Long,
        answer: String,
        emotion: String,
        isEditMode: Boolean
    ): Result<Unit> = runCatching {
        val signedUrl = questBehaviorRepository.requestQuestSignedUrl(
            SignedUrlRequestModel(contentType, imageKey)
        ).getOrThrow()

        questBehaviorRepository.uploadImageToSignedUrl(signedUrl, imageBytes, contentType)

        val request = BehaviorAnswerRequestModel(
            answer = answer,
            questEmotionState = emotion,
            imageKey = imageKey
        )

        val editRequest = QuestBehaviorEditModel(
            answer = answer,
            imageKey = imageKey
        )

        if (isEditMode) {
            questBehaviorRepository.updateQuestBehavior(questId = questId, request = editRequest)
        } else {
            questBehaviorRepository.uploadQuestBehaviorAnswer(questId, request)
        }
    }
}
