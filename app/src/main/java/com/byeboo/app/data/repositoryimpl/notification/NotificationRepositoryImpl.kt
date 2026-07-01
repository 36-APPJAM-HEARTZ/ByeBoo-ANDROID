package com.byeboo.app.data.repositoryimpl.notification

import com.byeboo.app.core.util.ErrorParser
import com.byeboo.app.data.datasource.remote.notification.NotificationDataSource
import com.byeboo.app.data.mapper.notification.toDomain
import com.byeboo.app.domain.model.notification.NotificationModel
import com.byeboo.app.domain.notification.NotificationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class NotificationRepositoryImpl
    @Inject
    constructor(
        private val notificationDataSource: NotificationDataSource,
    ) : NotificationRepository {
        private val _hasUnreadFlow = MutableStateFlow(false)
        override val hasUnreadFlow: StateFlow<Boolean> = _hasUnreadFlow.asStateFlow()

        override suspend fun checkHasUnreadNotifications(): Result<Boolean> =
            runCatching {
                val response = notificationDataSource.checkHasUnreadNotifications()
                if (!response.success) throw Exception(response.message)
                _hasUnreadFlow.value = response.data.hasUnread
                response.data.hasUnread
            }.fold(
                onSuccess = { Result.success(it) },
                onFailure = { Result.failure(Exception(ErrorParser.getErrorMessage(it))) },
            )

        override suspend fun getNotificationList(): Result<List<NotificationModel>> =
            runCatching {
                val response = notificationDataSource.getNotificationList()

                if (!response.success) throw Exception(response.message)
                response.data.notifications.map { it.toDomain() }
            }.fold(
                onSuccess = { Result.success(it) },
                onFailure = { Result.failure(Exception(ErrorParser.getErrorMessage(it))) },
            )

        override suspend fun markAllNotificationsAsRead(): Result<Unit> =
            runCatching {
                val response = notificationDataSource.patchAllNotificationsRead()

                if (!response.success) throw Exception(response.message)
                _hasUnreadFlow.value = false
                return@runCatching
            }.fold(
                onSuccess = { Result.success(it) },
                onFailure = { Result.failure(Exception(ErrorParser.getErrorMessage(it))) },
            )

        override suspend fun markNotificationAsRead(notificationId: Long): Result<Unit> =
            runCatching {
                val response = notificationDataSource.patchNotificationRead(notificationId)

                if (!response.success) throw Exception(response.message)
                checkHasUnreadNotifications()
                return@runCatching
            }.fold(
                onSuccess = { Result.success(it) },
                onFailure = { Result.failure(Exception(ErrorParser.getErrorMessage(it))) },
            )
    }
