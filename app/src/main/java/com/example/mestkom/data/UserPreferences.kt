package com.example.mestkom.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences (
    context: Context
) {
    private val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name = "Settings")
    private val dataStore = context.dataStore


    val authToken: Flow<String?>
    get() = dataStore.data.map { preferences ->
        preferences[KEY_AUTH]
    }

    suspend fun saveAuthToken(authToken: String) {
        dataStore.edit { preferences ->
                preferences[KEY_AUTH] = authToken
        }
    }

    suspend fun clear() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object{
        private val KEY_AUTH = stringPreferencesKey("key_auth")
    }
}