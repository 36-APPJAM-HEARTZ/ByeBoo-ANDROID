package com.byeboo.app.domain.repository.fcm

interface FcmTokenRepository {
    suspend fun saveFcmToken(token: String)
    suspend fun getFcmToken(): String?
}