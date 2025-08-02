package com.byeboo.app.domain.repository.auth

import com.byeboo.app.core.model.auth.UserEntity
import com.byeboo.app.domain.model.auth.UserInfoModel
import com.byeboo.app.domain.model.auth.UserJourney
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun updateUserInfo(userInfo: UserInfoModel): Result<UserEntity>
    suspend fun getUserEntity(): UserEntity
    suspend fun getUserJourney(): Result<UserJourney>
    suspend fun isLoggedIn(): Boolean
    fun getNickname(): Flow<String?>
    suspend fun getUserId(): Long?
    suspend fun clear()
}
