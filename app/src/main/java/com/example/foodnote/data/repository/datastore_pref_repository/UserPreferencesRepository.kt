package com.example.foodnote.data.repository.datastore_pref_repository

import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    val userId: Flow<String>
    suspend fun setUserId(userId: String)

    val theme: Flow<String>
    suspend fun setTheme(theme: String)

    val avatar: Flow<String>
    suspend fun setAvatar(theme: String)
}
