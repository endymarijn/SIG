package com.sportsnutrition.domain.repository

import com.sportsnutrition.domain.models.NutritionGoals
import com.sportsnutrition.domain.models.User
import com.sportsnutrition.utils.Result
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun register(name: String, email: String, password: String): Result<User>
    suspend fun login(email: String, password: String): Result<User>
    suspend fun getMe(): Result<Pair<User, NutritionGoals?>>
    fun getAuthToken(): Flow<String?>
    suspend fun logout()
    suspend fun isLoggedIn(): Boolean
}
