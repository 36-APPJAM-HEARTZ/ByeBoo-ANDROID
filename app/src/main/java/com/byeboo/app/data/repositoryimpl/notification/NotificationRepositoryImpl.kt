package com.byeboo.app.data.repositoryimpl.notification

import com.byeboo.app.data.datasource.remote.notification.NotificationDataSource
import com.byeboo.app.data.mapper.notification.toDomain
import com.byeboo.app.domain.model.notification.NotificationModel
import com.byeboo.app.domain.notification.NotificationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class NotificationRepositoryImpl
@Inject constructor(
    private val notificationDataSource: NotificationDataSource
) : NotificationRepository {

    private val _hasUnreadFlow = MutableStateFlow(false)
    override val hasUnreadFlow: StateFlow<Boolean> = _hasUnreadFlow.asStateFlow()

    override suspend fun checkHasUnreadNotifications(): Result<Boolean> =
        runCatching {
            val response = notificationDataSource.checkHasUnreadNotifications()
            if (response.success) {
                _hasUnreadFlow.value = response.data.hasUnread
                response.data.hasUnread
            } else {
                throw Exception(response.message)
            }
        }

    override suspend fun getNotificationList(): Result<List<NotificationModel>> =
        runCatching {
            val response = notificationDataSource.getNotificationList()

            if (response.success) {
                response.data.notifications.map { it.toDomain() }
            } else {
                throw Exception(response.message)
            }
        }

    override suspend fun markAllNotificationsAsRead(): Result<Unit> =
        runCatching {
            val response = notificationDataSource.patchAllNotificationsRead()

            if (response.success) {
                _hasUnreadFlow.value = false
                return@runCatching
            } else {
                throw Exception(response.message)
            }
        }

    override suspend fun markNotificationAsRead(notificationId: Long): Result<Unit> =
        runCatching {
            val response = notificationDataSource.patchNotificationRead(notificationId)

            if (response.success) {
                checkHasUnreadNotifications()
                return@runCatching
            } else {
                throw Exception(response.message)
            }
        }

}
