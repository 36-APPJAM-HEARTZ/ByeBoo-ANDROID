package com.byeboo.app.domain.repository.fcm

import com.byeboo.app.domain.model.notification.FcmTokenModel
import com.byeboo.app.domain.model.notification.NotificationSetting

interface FcmTokenRepository {
    suspend fun saveFcmToken(fcmToken: FcmTokenModel): Result<Unit>

    suspend fun updateFcmToken(fcmToken: FcmTokenModel): Result<Unit>

    suspend fun deleteFcmToken(fcmToken: FcmTokenModel): Result<Unit>

    suspend fun allowQuestAlarm(): Result<NotificationSetting>

    suspend fun getFcmToken(): String?

    suspend fun saveAlarmEnabled(isAlarmEnabled: Boolean): Result<Unit>

    suspend fun isAlarmEnabled(): Boolean
}
