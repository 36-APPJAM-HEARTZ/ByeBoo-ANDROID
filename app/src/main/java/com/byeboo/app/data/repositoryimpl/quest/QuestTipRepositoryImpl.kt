package com.byeboo.app.data.repositoryimpl.quest

import com.byeboo.app.data.datasource.remote.quest.QuestTipDataSource
import com.byeboo.app.data.mapper.quest.toDomain
import com.byeboo.app.domain.model.quest.QuestTip
import com.byeboo.app.domain.repository.quest.QuestTipRepository
import javax.inject.Inject

class QuestTipRepositoryImpl
    @Inject
    constructor(
        private val questTipDataSource: QuestTipDataSource,
    ) : QuestTipRepository {
        override suspend fun getQuestTip(questId: Long): Result<QuestTip> =
            runCatching {
                val response = questTipDataSource.getQuestTip(questId)
                response.data.toDomain()
            }
    }
