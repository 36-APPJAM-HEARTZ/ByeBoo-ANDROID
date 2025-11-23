package com.byeboo.app.data.repositoryimpl.fcm

import com.byeboo.app.data.datasource.local.FcmLocalDataSource
import com.byeboo.app.domain.repository.fcm.FcmTokenRepository
import javax.inject.Inject

class FcmTokenRepositoryImpl @Inject constructor(
    private val fcmLocalDataSource: FcmLocalDataSource
): FcmTokenRepository {
    override suspend fun saveFcmToken(token: String) {
        fcmLocalDataSource.saveFcmToken(token)
    }

    override suspend fun getFcmToken(): String? {
        return fcmLocalDataSource.getFcmToken()
    }
}