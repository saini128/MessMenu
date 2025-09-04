package com.singhropar.messmenu.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.Flow

val Context.dataStore by preferencesDataStore("mess_prefs")

class UserPreferences(private val context: Context) {
    companion object {
        val SELECTED_HOSTEL = stringPreferencesKey("selected_hostel")
        val CACHED_MENU = stringPreferencesKey("cached_menu")
    }

    fun getSelectedHostel(): Flow<String?> =
        context.dataStore.data.map { it[SELECTED_HOSTEL] }

    suspend fun saveSelectedHostel(hostel: String) {
        context.dataStore.edit { prefs ->
            prefs[SELECTED_HOSTEL] = hostel
        }
    }

    fun getCachedMenu(): Flow<String?> =
        context.dataStore.data.map { it[CACHED_MENU] }

    suspend fun saveCachedMenu(json: String) {
        context.dataStore.edit { prefs ->
            prefs[CACHED_MENU] = json
        }
    }
}
