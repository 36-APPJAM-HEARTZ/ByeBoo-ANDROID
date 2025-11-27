package com.byeboo.app.data.repositoryimpl.fcm

import com.byeboo.app.data.datasource.local.FcmLocalDataSource
import com.byeboo.app.data.datasource.remote.fcm.FcmRemoteDataSource
import com.byeboo.app.data.mapper.notification.toData
import com.byeboo.app.data.mapper.notification.toDomain
import com.byeboo.app.domain.model.notification.FcmTokenModel
import com.byeboo.app.domain.model.notification.NotificationSetting
import com.byeboo.app.domain.repository.fcm.FcmTokenRepository
import javax.inject.Inject

class FcmTokenRepositoryImpl @Inject constructor(
    private val fcmLocalDataSource: FcmLocalDataSource,
    private val fcmRemoteDataSource: FcmRemoteDataSource
): FcmTokenRepository {
    override suspend fun saveFcmToken(fcmToken: FcmTokenModel): Result<Unit> {
        return runCatching {
            fcmRemoteDataSource.saveFcmToken(fcmToken.toData())
            fcmLocalDataSource.saveFcmToken(fcmToken.token)

        }
    }

    override suspend fun updateFcmToken(fcmToken: FcmTokenModel): Result<Unit> {
        return runCatching {
            fcmRemoteDataSource.updateFcmToken(fcmToken.toData())
            fcmLocalDataSource.saveFcmToken(fcmToken.token)
        }
    }

    override suspend fun deleteFcmToken(fcmToken: FcmTokenModel): Result<Unit> {
        return runCatching {
            fcmRemoteDataSource.deleteFcmToken(fcmToken.toData())
            fcmLocalDataSource.deleteFcmToken()

        }
    }

    override suspend fun allowQuestAlarm(): Result<NotificationSetting> {
        return runCatching {
            val response = fcmRemoteDataSource.allowQuestAlarm()
            response.data.toDomain()
        }
    }

    override suspend fun getFcmToken(): String? {
        return fcmLocalDataSource.getFcmToken()
    }

    override suspend fun saveAlarmEnabled(isAlarmEnabled: Boolean): Result<Unit> {
        return runCatching {
            fcmLocalDataSource.saveAlarmEnabled(isAlarmEnabled)
        }
    }

    override suspend fun isAlarmEnabled(): Boolean {
        return fcmLocalDataSource.isAlarmEnabled()
    }
}