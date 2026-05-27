package com.byeboo.app.data.repositoryimpl.quest

import com.byeboo.app.core.util.ErrorParser
import com.byeboo.app.data.datasource.remote.quest.QuestCommonDataSource
import com.byeboo.app.data.dto.request.quest.QuestCommentReplyRequestDto
import com.byeboo.app.data.dto.request.quest.QuestCommonCommentRequestDto
import com.byeboo.app.data.mapper.quest.toData
import com.byeboo.app.data.mapper.quest.toDomain
import com.byeboo.app.domain.model.quest.CommentRepliesModel
import com.byeboo.app.domain.model.quest.CommonQuestModel
import com.byeboo.app.domain.model.quest.QuestAnswerDetailModel
import com.byeboo.app.domain.model.quest.QuestAnswerModel
import com.byeboo.app.domain.model.quest.QuestCommonAnswerEditModel
import com.byeboo.app.domain.model.quest.QuestCommonAnswerRequestModel
import com.byeboo.app.domain.repository.quest.QuestCommonRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class QuestCommonRepositoryImpl
    @Inject
    constructor(
        private val questCommonDataSource: QuestCommonDataSource,
    ) : QuestCommonRepository {
        private val _answersFlow = MutableStateFlow<List<QuestAnswerModel>>(emptyList())
        override val answersFlow: StateFlow<List<QuestAnswerModel>> = _answersFlow.asStateFlow()

        private val _answerSubmittedEvent = MutableSharedFlow<Long>()
        override val answerSubmittedEvent: SharedFlow<Long> = _answerSubmittedEvent.asSharedFlow()

        private val _refreshEvent = MutableSharedFlow<Unit>()
        override val refreshEvent: SharedFlow<Unit> = _refreshEvent.asSharedFlow()

        private var currentCursor: Long? = null
        private var hasNextPage: Boolean = true

        override suspend fun uploadQuestCommonAnswer(
            questId: Long,
            request: QuestCommonAnswerRequestModel,
        ): Result<Unit> =
            runCatching {
                val response =
                    questCommonDataSource.uploadQuestCommonAnswer(
                        questId = questId,
                        request = request.toData(),
                    )
                if (!response.success) throw Exception(response.message)
                _answerSubmittedEvent.emit(questId)
            }.fold(
                onSuccess = { Result.success(Unit) },
                onFailure = { Result.failure(Exception(ErrorParser.getErrorMessage(it))) },
            )

        override suspend fun refreshMyAnswers(): Result<Unit> =
            runCatching {
                currentCursor = null
                hasNextPage = true
                _answersFlow.update { emptyList() }
                fetchMyAnswers()
            }.fold(
                onSuccess = { Result.success(Unit) },
                onFailure = { Result.failure(Exception(ErrorParser.getErrorMessage(it))) },
            )

        override suspend fun loadMyAnswers(): Result<Unit> =
            runCatching {
                if (!hasNextPage) return Result.success(Unit)
                fetchMyAnswers()
            }.fold(
                onSuccess = { Result.success(Unit) },
                onFailure = { Result.failure(Exception(ErrorParser.getErrorMessage(it))) },
            )

        private suspend fun fetchMyAnswers() {
            val response = questCommonDataSource.getQuestCommonMyAnswer(currentCursor)
            if (!response.success) throw Exception(response.message)
            val domainModel = response.data.toDomain()
            _answersFlow.update { current -> current + domainModel.answers }
            currentCursor = domainModel.nextCursor
            hasNextPage = domainModel.hasNext
        }

        override suspend fun patchQuestCommonAnswer(
            answerId: Long,
            request: QuestCommonAnswerEditModel,
        ): Result<Unit> =
            runCatching {
                val response =
                    questCommonDataSource.patchQuestCommonAnswer(
                        answerId = answerId,
                        request = request.toData(),
                    )
                if (!response.success) throw Exception(response.message)

                val exists = _answersFlow.value.any { it.answerId == answerId }
                if (exists) {
                    _answersFlow.update { currentList ->
                        currentList.map { item ->
                            if (item.answerId == answerId) item.copy(content = request.answer) else item
                        }
                    }
                }
                _refreshEvent.emit(Unit)
            }.fold(
                onSuccess = { Result.success(Unit) },
                onFailure = { Result.failure(Exception(ErrorParser.getErrorMessage(it))) },
            )

        override suspend fun deleteQuestCommonAnswer(answerId: Long): Result<Unit> =
            runCatching {
                val response = questCommonDataSource.deleteQuestCommonAnswer(answerId = answerId)
                if (!response.success) throw Exception(response.message)
                _answersFlow.update { currentList ->
                    currentList.filter { it.answerId != answerId }
                }
                _refreshEvent.emit(Unit)
            }.fold(
                onSuccess = { Result.success(Unit) },
                onFailure = { Result.failure(Exception(ErrorParser.getErrorMessage(it))) },
            )

        override suspend fun getCommonQuests(
            date: String,
            cursor: Long?,
            limit: Int,
        ): Result<CommonQuestModel> =
            runCatching {
                val response = questCommonDataSource.getCommonQuests(date, cursor, limit)
                if (!response.success) throw Exception(response.message)
                response.data.toDomain()
            }.fold(
                onSuccess = { Result.success(it) },
                onFailure = { Result.failure(Exception(ErrorParser.getErrorMessage(it))) },
            )

        override suspend fun getCommonQuestAnswerDetail(answerId: Long): Result<QuestAnswerDetailModel> =
            runCatching {
                val response = questCommonDataSource.getQuestCommonAnswerDetail(answerId)
                if (!response.success) throw Exception(response.message)
                response.data.toDomain()
            }.fold(
                onSuccess = { Result.success(it) },
                onFailure = { Result.failure(Exception(ErrorParser.getErrorMessage(it))) },
            )

        override suspend fun updateBlockedUser(blockedUserId: Long): Result<Unit> =
            runCatching {
                val response = questCommonDataSource.updateBlockedUser(blockedUserId)
                if (!response.success) throw Exception(response.message)
                _refreshEvent.emit(Unit)
            }.fold(
                onSuccess = { Result.success(Unit) },
                onFailure = { Result.failure(Exception(ErrorParser.getErrorMessage(it))) },
            )

        override suspend fun reportCommonQuest(answerId: Long): Result<Unit> =
            runCatching {
                val response = questCommonDataSource.reportCommonQuest(answerId)
                if (!response.success) throw Exception(response.message)
                _refreshEvent.emit(Unit)
            }.fold(
                onSuccess = { Result.success(Unit) },
                onFailure = { Result.failure(Exception(ErrorParser.getErrorMessage(it))) },
            )

    override suspend fun uploadComment(
        content: String,
        targetId: Long,
    ): Result<Unit> = runCatching {
        val response = questCommonDataSource.uploadComment(
            QuestCommonCommentRequestDto(
                content = content,
                targetId = targetId,
            )
        )
        if (!response.success) throw Exception(response.message)
    }.fold(
        onSuccess = { Result.success(Unit) },
        onFailure = { Result.failure(Exception(ErrorParser.getErrorMessage(it))) },
    )

    override suspend fun getCommentReplies(commentId: Long): Result<CommentRepliesModel> =
        runCatching {
            val response = questCommonDataSource.getCommentReplies(commentId)
            if (!response.success) throw Exception(response.message)
            response.data.toDomain()
        }.fold(
            onSuccess = { Result.success(it) },
            onFailure = { Result.failure(Exception(ErrorParser.getErrorMessage(it))) },
        )

    override suspend fun uploadCommentReply(
        commentId: Long,
        content: String,
    ): Result<Unit> = runCatching {
        val response = questCommonDataSource.uploadCommentReply(
            commentId = commentId,
            request = QuestCommentReplyRequestDto(content = content),
        )
        if (!response.success) throw Exception(response.message)
    }.fold(
        onSuccess = { Result.success(Unit) },
        onFailure = { Result.failure(Exception(ErrorParser.getErrorMessage(it))) },
    )

        override fun getCachedMyAnswer(answerId: Long): QuestAnswerModel? = _answersFlow.value.find { it.answerId == answerId }
    }
