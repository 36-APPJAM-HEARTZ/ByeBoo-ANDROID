package com.byeboo.app.data.datasource.local

interface FcmLocalDataSource {
    suspend fun saveFcmToken(token: String)
    suspend fun getFcmToken(): String?
}