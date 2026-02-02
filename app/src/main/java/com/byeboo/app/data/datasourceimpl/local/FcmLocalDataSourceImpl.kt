package com.byeboo.app.data.datasourceimpl.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.byeboo.app.data.datasource.local.FcmLocalDataSource
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class FcmLocalDataSourceImpl
@Inject
constructor(
    private val dataStore: DataStore<Preferences>
) : FcmLocalDataSource {
    override suspend fun saveFcmToken(token: String) {
        dataStore.edit { preferences ->
            preferences[FCM_TOKEN] = token
        }
    }

    override suspend fun getFcmToken(): String? =
        dataStore.data
            .map { preferences ->
                preferences[FCM_TOKEN].orEmpty()
            }.first()

    override suspend fun deleteFcmToken() {
        dataStore.edit { preferences ->
            preferences.remove(FCM_TOKEN)
        }
    }

    override suspend fun saveAlarmEnabled(isAlarmEnabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[ALARM_ENABLED] = isAlarmEnabled
        }
    }

    override suspend fun isAlarmEnabled(): Boolean =
        dataStore.data
            .map { preferences ->
                preferences[ALARM_ENABLED] ?: false
            }.first()

    companion object {
        private val FCM_TOKEN = stringPreferencesKey("fcm_Token")
        private val ALARM_ENABLED = booleanPreferencesKey("alarm_enabled")
    }
}
