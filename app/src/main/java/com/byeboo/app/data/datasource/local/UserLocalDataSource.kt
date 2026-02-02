package com.byeboo.app.data.datasource.local

import com.byeboo.app.core.model.auth.UserEntity
import com.byeboo.app.domain.model.JourneyStatusType
import kotlinx.coroutines.flow.Flow

interface UserLocalDataSource {
    suspend fun getUserEntity(): UserEntity

    suspend fun getUserId(): Long?

    fun getNickname(): Flow<String>

    suspend fun saveId(userId: Long)

    suspend fun saveNickname(nickname: String)

    suspend fun setLoggedIn(loggedIn: Boolean)

    suspend fun isLoggedIn(): Boolean

    suspend fun setQuestStarted(started: Boolean)

    suspend fun saveJourney(journey: String)

    suspend fun getJourney(): String?

    suspend fun saveJourneyStatus(journeyStatus: JourneyStatusType)

    fun getJourneyStatus(): Flow<JourneyStatusType>

    suspend fun setHasSeenAboutHelp(seen: Boolean)

    suspend fun hasSeenAboutHelp(): Boolean

    suspend fun clear()

    suspend fun isUserRegistered(): Boolean

    suspend fun setUserRegistered(isRegistered: Boolean)

    fun getLoggedIn(): Flow<Boolean>
}
