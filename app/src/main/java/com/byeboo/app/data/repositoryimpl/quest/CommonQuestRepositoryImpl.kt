package com.byeboo.app.data.repositoryimpl.quest

import com.byeboo.app.data.datasource.remote.quest.CommonQuestDataSource
import com.byeboo.app.data.mapper.quest.toDomain
import com.byeboo.app.domain.model.quest.CommonQuestModel
import com.byeboo.app.domain.repository.quest.CommonQuestRepository
import javax.inject.Inject

class CommonQuestRepositoryImpl
    @Inject
    constructor(
        private val commonQuestDataSource: CommonQuestDataSource,
    ) : CommonQuestRepository {
        override suspend fun getCommonQuests(
            date: String,
            cursor: Long?,
            limit: Int,
        ): Result<CommonQuestModel> =
            runCatching {
                val response = commonQuestDataSource.getCommonQuests(date, cursor, limit)
                response.data.toDomain()
            }
    }
