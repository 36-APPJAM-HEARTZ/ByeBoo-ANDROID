package com.byeboo.app.domain.repository.config

interface ConfigRepository {
    suspend fun getMinVersionCode(): Int
    fun isUpdateRequired(minVersion: Int): Boolean
}