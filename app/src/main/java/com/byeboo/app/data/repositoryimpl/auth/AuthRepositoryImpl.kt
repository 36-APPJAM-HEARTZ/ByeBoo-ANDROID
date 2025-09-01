package com.byeboo.app.data.repositoryimpl.auth

import com.byeboo.app.core.model.auth.TokenEntity
import com.byeboo.app.data.datasource.remote.auth.AuthRemoteDataSource
import com.byeboo.app.data.mapper.auth.toDomain
import com.byeboo.app.domain.model.auth.AuthResult
import com.byeboo.app.domain.repository.auth.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource
) : AuthRepository {
    override suspend fun loginWithKakao(token: String, platform: String): Result<AuthResult> =
        runCatching {
            authRemoteDataSource.loginWithKakao(
                token = token,
                platform = platform
            ).data.toDomain()
        }

    override suspend fun reissueAccessToken(refreshToken: String): Result<TokenEntity> =
        runCatching {
            authRemoteDataSource.reissueAccessToken(refreshToken).data.toDomain()
        }
}