package com.example.gardenbuddy.utils

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferences @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val USER_ID_KEY = stringPreferencesKey("user_id")
        private val USERNAME_KEY = stringPreferencesKey("username")
    }

    // Salva i dati dell'utente nel DataStore
    suspend fun saveUser(userId: String, username: String) {
        dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = userId
            preferences[USERNAME_KEY] = username
        }
    }

    suspend fun getUserIdOnce(): String? {
        return dataStore.data.map { preferences ->
            preferences[USER_ID_KEY]
        }.first()
    }

    // Recupera lo userId come Flow (opzionale)
    val userIdFlow: Flow<String?> = dataStore.data
        .map { preferences ->
            preferences[USER_ID_KEY]
        }

    // Recupera lo username come Flow (opzionale)
    val usernameFlow: Flow<String?> = dataStore.data
        .map { preferences ->
            preferences[USERNAME_KEY]
        }
}

