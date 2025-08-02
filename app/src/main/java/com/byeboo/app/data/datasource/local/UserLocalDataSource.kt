package com.byeboo.app.data.datasource.local

import com.byeboo.app.core.model.auth.UserEntity
import kotlinx.coroutines.flow.Flow

interface UserLocalDataSource {
    suspend fun getUserEntity(): UserEntity
    suspend fun getUserId(): Long?
    fun getNickname(): Flow<String?>
    suspend fun saveId(userId: Long)
    suspend fun saveNickname(nickname: String)
    suspend fun setLoggedIn(loggedIn: Boolean)
    suspend fun isLoggedIn(): Boolean
    suspend fun setQuestStarted(started: Boolean)
    suspend fun isQuestStarted(): Boolean
    suspend fun saveJourney(journey: String)
    suspend fun getJourney(): String?
    suspend fun clear()
}
