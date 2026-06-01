package com.sportsnutrition.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.sportsnutrition.utils.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = Constants.DATASTORE_NAME)

@Singleton
class TokenDataStore @Inject constructor(@ApplicationContext private val context: Context) {

    private val tokenKey = stringPreferencesKey(Constants.KEY_AUTH_TOKEN)
    private val userIdKey = stringPreferencesKey(Constants.KEY_USER_ID)

    val authToken: Flow<String?> = context.dataStore.data.map { prefs -> prefs[tokenKey] }
    val userId: Flow<String?> = context.dataStore.data.map { prefs -> prefs[userIdKey] }

    suspend fun saveToken(token: String) {
        context.dataStore.edit { it[tokenKey] = token }
    }

    suspend fun saveUserId(id: String) {
        context.dataStore.edit { it[userIdKey] = id }
    }

    suspend fun clearAll() {
        context.dataStore.edit { it.clear() }
    }
}
