package com.byeboo.app.data.datasourceimpl.local

import com.byeboo.app.data.datasource.local.FcmLocalDataSource
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class FcmLocalDataSourceImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : FcmLocalDataSource{
    override suspend fun saveFcmToken(token: String) {
        dataStore.edit { prefs ->
            prefs[FCM_TOKEN] = token
        }
    }

    override suspend fun getFcmToken(): String? {
        val prefs = dataStore.data.first()
        return prefs[FCM_TOKEN].orEmpty()
    }

    companion object {
       private val FCM_TOKEN = stringPreferencesKey("fcm_Token")
    }
}