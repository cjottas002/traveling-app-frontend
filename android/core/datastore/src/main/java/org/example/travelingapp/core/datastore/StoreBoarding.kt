package org.example.travelingapp.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.boardingDataStore: DataStore<Preferences> by preferencesDataStore("storeBoarding")

class StoreBoarding(private val context: Context) {

    companion object {
        val STORE_BOARD = booleanPreferencesKey("store_board")
    }

    val getBoarding: Flow<Boolean> = context.boardingDataStore.data
        .map { prefs -> prefs[STORE_BOARD] == true }

    suspend fun saveBoarding(value: Boolean) {
        context.boardingDataStore.edit { prefs ->
            prefs[STORE_BOARD] = value
        }
    }
}


