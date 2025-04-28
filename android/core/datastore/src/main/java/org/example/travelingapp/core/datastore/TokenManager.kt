package org.example.travelingapp.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.authDataStore: DataStore<Preferences> by preferencesDataStore("auth_prefs")

@Singleton
class TokenManager @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    companion object {
        private val KEY_AUTH_TOKEN = stringPreferencesKey("KEY_AUTH_TOKEN")
    }

    private val authToken: Flow<String?> = context.authDataStore.data
        .map { prefs -> prefs[KEY_AUTH_TOKEN] }

    suspend fun saveToken(token: String) {
        context.authDataStore.edit { prefs ->
            prefs[KEY_AUTH_TOKEN] = token
        }
    }

    suspend fun fetchToken(): String? {
        return authToken.first()
    }

    suspend fun clearToken() {
        context.authDataStore.edit { prefs ->
            prefs.remove(KEY_AUTH_TOKEN)
        }
    }
}
