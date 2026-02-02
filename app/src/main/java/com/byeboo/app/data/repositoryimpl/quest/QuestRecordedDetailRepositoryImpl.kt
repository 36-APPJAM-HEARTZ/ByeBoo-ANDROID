package com.byeboo.app.data.repositoryimpl.quest

import com.byeboo.app.data.datasource.remote.quest.QuestRecordedDetailDataSource
import com.byeboo.app.data.mapper.quest.toDomain
import com.byeboo.app.domain.model.quest.QuestRecordedDetailModel
import com.byeboo.app.domain.repository.quest.QuestRecordedDetailRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update

class QuestRecordedDetailRepositoryImpl
@Inject
constructor(
    private val questRecordedDetailDataSource: QuestRecordedDetailDataSource
) : QuestRecordedDetailRepository {
    private val cache = MutableStateFlow<Map<Long, QuestRecordedDetailModel>>(emptyMap())

    override suspend fun getQuestRecordedDetail(questId: Long): Result<QuestRecordedDetailModel> =
        runCatching {
            val response = questRecordedDetailDataSource.getQuestRecordedDetail(questId)
            val detail = response.data.toDomain()

            cache.update { old ->
                old + (questId to detail)
            }

            detail
        }

    override fun observeQuestRecordedDetail(questId: Long): Flow<QuestRecordedDetailModel> =
        cache
            .mapNotNull { it[questId] }
            .onStart {
                getQuestRecordedDetail(questId)
            }
}
