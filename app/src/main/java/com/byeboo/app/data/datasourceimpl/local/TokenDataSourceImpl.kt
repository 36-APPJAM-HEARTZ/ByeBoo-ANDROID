package com.byeboo.app.data.datasourceimpl.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.byeboo.app.data.datasource.local.TokenDataSource
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TokenDataSourceImpl @Inject constructor(
    private val datastore: DataStore<Preferences>
) : TokenDataSource {

    override fun getAccessToken(): Flow<String> = datastore.data.map {
            preferences ->
        preferences[ACCESS_TOKEN] ?: ""
    }

    override fun getRefreshToken(): Flow<String> = datastore.data.map {
            preferences ->
        preferences[REFRESH_TOKEN] ?: ""
    }

    override suspend fun updateTokens(accessToken: String, refreshToken: String) {
        datastore.edit { preferences ->
            preferences[ACCESS_TOKEN] = accessToken
            preferences[REFRESH_TOKEN] = refreshToken
        }
    }

    override suspend fun clearTokens() {
        datastore.edit { preferences ->
            preferences.remove(ACCESS_TOKEN)
            preferences.remove(REFRESH_TOKEN)
        }
    }

    override suspend fun setLoginSplash(show: Boolean) {
        datastore.edit { it[SHOW_LOGIN_BUTTON] = show }
    }

    override suspend fun restartSplash(): Boolean {
        var showButton = false
        datastore.edit {
            showButton = it[SHOW_LOGIN_BUTTON] ?: false
            if (showButton) it[SHOW_LOGIN_BUTTON] = false
        }
        return showButton
    }

    companion object {
        private val ACCESS_TOKEN = stringPreferencesKey("ACCESS_TOKEN")
        private val REFRESH_TOKEN = stringPreferencesKey("REFRESH_TOKEN")
        private val SHOW_LOGIN_BUTTON = booleanPreferencesKey("SHOW_LOGIN_BUTTON")
    }
}
