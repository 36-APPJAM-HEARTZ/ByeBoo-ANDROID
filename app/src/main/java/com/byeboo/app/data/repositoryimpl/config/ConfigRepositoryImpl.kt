package com.byeboo.app.data.repositoryimpl.config

import com.byeboo.app.BuildConfig
import com.byeboo.app.domain.repository.config.ConfigRepository
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ConfigRepositoryImpl
    @Inject
    constructor(
        private val remoteConfig: FirebaseRemoteConfig,
    ) : ConfigRepository {
        override suspend fun getMinVersionCode(): Int =
            runCatching {
                remoteConfig.fetchAndActivate().await()
                remoteConfig.getLong("min_version_code").toInt()
            }.getOrDefault(0)

        override fun isUpdateRequired(minVersion: Int): Boolean = BuildConfig.VERSION_CODE < minVersion
    }
