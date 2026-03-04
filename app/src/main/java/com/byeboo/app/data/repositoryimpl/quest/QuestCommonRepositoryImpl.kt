package com.byeboo.app.data.repositoryimpl.quest

import com.byeboo.app.data.datasource.remote.quest.QuestCommonDataSource
import com.byeboo.app.data.mapper.quest.toData
import com.byeboo.app.data.mapper.quest.toDomain
import com.byeboo.app.domain.model.quest.QuestAnswerModel
import com.byeboo.app.domain.model.quest.QuestCommonAnswerEditModel
import com.byeboo.app.domain.model.quest.QuestCommonAnswerRequestModel
import com.byeboo.app.domain.model.quest.QuestCommonMyAnswerModel
import com.byeboo.app.domain.repository.quest.QuestCommonRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class QuestCommonRepositoryImpl @Inject constructor(
    private val questCommonDataSource: QuestCommonDataSource,
) : QuestCommonRepository {

    private val _answersFlow = MutableStateFlow<List<QuestAnswerModel>>(emptyList())
    override val answersFlow: StateFlow<List<QuestAnswerModel>>
        get() = _answersFlow.asStateFlow()

    override suspend fun uploadQuestCommonAnswer(
        questId: Long,
        request: QuestCommonAnswerRequestModel
    ): Result<Unit> =
        runCatching {
            questCommonDataSource.uploadQuestCommonAnswer(
                questId = questId,
                request = request.toData()
            )
        }

    override suspend fun getQuestCommonMyAnswer(cursor: Long?): Result<QuestCommonMyAnswerModel> =
        runCatching {
            val response = questCommonDataSource.getQuestCommonMyAnswer(cursor)
            val domainModel = response.data.toDomain()

            _answersFlow.update { currentList ->
                if (cursor == null){
                    domainModel.answers
                } else {
                    currentList + domainModel.answers
                }
            }

            domainModel
        }

    override suspend fun patchQuestCommonAnswer(
        answerId: Long,
        request: QuestCommonAnswerEditModel
    ): Result<Unit> =
        runCatching {
            questCommonDataSource.patchQuestCommonAnswer(
                answerId = answerId,
                request = request.toData()
            )
        }

    override suspend fun deleteQuestCommonAnswer(answerId: Long): Result<Unit> =
        runCatching {
            questCommonDataSource.deleteQuestCommonAnswer(
                answerId = answerId
            )

            _answersFlow.update { currentList ->
                currentList.filter { it.answerId != answerId }
            }

        }

    override fun getCachedMyAnswer(answerId: Long): QuestAnswerModel? {
        return _answersFlow.value.find { it.answerId == answerId }
    }
}