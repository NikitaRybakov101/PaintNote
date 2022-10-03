package com.example.foodnote.data.repository.datastore_pref_repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.foodnote.data.repository.datastore_pref_repository.UserPreferencesRepositoryImpl.PreferencesKeys.AVATAR
import com.example.foodnote.data.repository.datastore_pref_repository.UserPreferencesRepositoryImpl.PreferencesKeys.THEME
import com.example.foodnote.data.repository.datastore_pref_repository.UserPreferencesRepositoryImpl.PreferencesKeys.USER_ID
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import java.io.IOException

class UserPreferencesRepositoryImpl(private val dataStore: DataStore<Preferences>) :
    UserPreferencesRepository {
    private object PreferencesKeys {
        val USER_ID = stringPreferencesKey(USER_ID_KEY)
        val THEME = stringPreferencesKey(KEY_THEME)
        val AVATAR = stringPreferencesKey(KEY_AVATAR)
    }

    override val userId: Flow<String> = dataStore.data
        .catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                Log.e(TAG, "Error reading preferences.", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            // No type safety.
            preferences[USER_ID] ?: ""
        }

    override val theme: Flow<String> = dataStore.data
        .catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                Log.e(TAG, "Error reading preferences.", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .distinctUntilChanged()
        .map { preferences ->
            // No type safety.
            preferences[THEME] ?: "light"
        }

    override val avatar: Flow<String> = dataStore.data
        .catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                Log.e(TAG, "Error reading preferences.", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .distinctUntilChanged()
        .map { preferences ->
            // No type safety.
            preferences[AVATAR] ?: "null"
        }

    override suspend fun setAvatar(path: String) {
        dataStore.edit { settings ->
            settings[AVATAR] = path
        }
    }

    override suspend fun setTheme(theme: String) {
        dataStore.edit { settings ->
            settings[THEME] = theme
        }
    }

    override suspend fun setUserId(userId: String) {
        dataStore.edit { settings ->
            settings[USER_ID] = userId
        }
    }

    private companion object {
        const val USER_ID_KEY = "USER_ID"
        const val KEY_THEME = "KEY_THEME"
        const val KEY_AVATAR = "KEY_AVATAR"
        const val TAG: String = "UserPreferencesRepo"
    }
}