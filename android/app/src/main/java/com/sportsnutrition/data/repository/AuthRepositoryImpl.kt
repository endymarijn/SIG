package com.sportsnutrition.data.repository

import com.sportsnutrition.data.api.ApiService
import com.sportsnutrition.data.api.dto.LoginRequest
import com.sportsnutrition.data.api.dto.RegisterRequest
import com.sportsnutrition.data.local.TokenDataStore
import com.sportsnutrition.data.models.toDomain
import com.sportsnutrition.domain.models.NutritionGoals
import com.sportsnutrition.domain.models.User
import com.sportsnutrition.domain.repository.AuthRepository
import com.sportsnutrition.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: ApiService,
    private val dataStore: TokenDataStore,
) : AuthRepository {

    override suspend fun register(name: String, email: String, password: String): Result<User> =
        runCatching {
            val response = api.register(RegisterRequest(name, email, password))
            val body = response.body()
            if (response.isSuccessful && body?.success == true && body.data != null) {
                dataStore.saveToken(body.data.token)
                dataStore.saveUserId(body.data.user.id)
                Result.Success(body.data.user.toDomain())
            } else {
                Result.Error(body?.message ?: "Registratie mislukt", response.code())
            }
        }.getOrElse { Result.Error(it.message ?: "Netwerkfout") }

    override suspend fun login(email: String, password: String): Result<User> =
        runCatching {
            val response = api.login(LoginRequest(email, password))
            val body = response.body()
            if (response.isSuccessful && body?.success == true && body.data != null) {
                dataStore.saveToken(body.data.token)
                dataStore.saveUserId(body.data.user.id)
                Result.Success(body.data.user.toDomain())
            } else {
                Result.Error(body?.message ?: "Inloggen mislukt", response.code())
            }
        }.getOrElse { Result.Error(it.message ?: "Netwerkfout") }

    override suspend fun getMe(): Result<Pair<User, NutritionGoals?>> =
        runCatching {
            val response = api.getMe()
            val body = response.body()
            if (response.isSuccessful && body?.success == true && body.data != null) {
                Result.Success(Pair(body.data.user.toDomain(), body.data.nutrition?.toDomain()))
            } else {
                Result.Error(body?.message ?: "Ophalen profiel mislukt")
            }
        }.getOrElse { Result.Error(it.message ?: "Netwerkfout") }

    override fun getAuthToken(): Flow<String?> = dataStore.authToken

    override suspend fun logout() = dataStore.clearAll()

    override suspend fun isLoggedIn(): Boolean = dataStore.authToken.first() != null
}
