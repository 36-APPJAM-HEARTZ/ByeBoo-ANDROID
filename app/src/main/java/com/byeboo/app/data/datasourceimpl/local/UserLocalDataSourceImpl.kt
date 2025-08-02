package com.byeboo.app.data.datasourceimpl.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.byeboo.app.core.model.auth.UserEntity
import com.byeboo.app.data.datasource.local.UserLocalDataSource
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class UserLocalDataSourceImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : UserLocalDataSource {
    override suspend fun getUserEntity(): UserEntity {
        return runCatching {
            val preferences = dataStore.data.first()
            UserEntity(
                nickname = preferences[NICKNAME],
                userId = preferences[USERID],
                isLoggedIn = preferences[IS_LOGGED_IN] ?: false
            )
        }.getOrElse {
            UserEntity()
        }
    }

    override suspend fun getUserId(): Long? {
        return runCatching {
            dataStore.data.first()[USERID]
        }.getOrNull()
    }

    override suspend fun isLoggedIn(): Boolean {
        return runCatching {
            dataStore.data.first()[IS_LOGGED_IN] ?: false
        }.getOrElse { false }
    }

    override fun getNickname(): Flow<String?> {
        return dataStore.data
            .map { preferences -> preferences[NICKNAME] }
            .catch { emit(null) }
    }

    override suspend fun setLoggedIn(loggedIn: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = loggedIn
        }
    }

    override suspend fun saveNickname(nickname: String) {
        dataStore.edit { preferences ->
            preferences[NICKNAME] = nickname
        }
    }

    override suspend fun saveId(userId: Long) {
        dataStore.edit { preferences ->
            preferences[USERID] = userId
        }
    }

    override suspend fun setQuestStarted(started: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_QUEST_STARTED] = started
        }
    }

    override suspend fun isQuestStarted(): Boolean {
        return dataStore.data.first()[IS_QUEST_STARTED] ?: false
    }

    override suspend fun saveJourney(journey: String) {
        dataStore.edit { it[JOURNEY] = journey }
    }

    override suspend fun getJourney(): String? {
        return dataStore.data.first()[JOURNEY]
    }

    override suspend fun clear() {
        runCatching {
            dataStore.edit { preferences ->
                preferences.clear()
            }
        }
    }

    companion object {
        private val IS_LOGGED_IN = booleanPreferencesKey("IS_LOGGED_IN")
        private val NICKNAME = stringPreferencesKey("NICKNAME")
        private val USERID = longPreferencesKey("USERID")
        private val IS_QUEST_STARTED = booleanPreferencesKey("IS_QUEST_STARTED")
        private val JOURNEY = stringPreferencesKey("JOURNEY")
    }
}
