package com.byeboo.app.domain.usecase.quest

import com.byeboo.app.domain.model.quest.QuestAiAnswerModel
import com.byeboo.app.domain.repository.quest.QuestAiAnswerRepository
import javax.inject.Inject

class PostAiAnswerUseCase
    @Inject
    constructor(
        private val questAiAnswerRepository: QuestAiAnswerRepository,
    ) {
        suspend operator fun invoke(questId: Long): Result<QuestAiAnswerModel> = questAiAnswerRepository.postAiAnswer(questId = questId)
    }
