package com.sportsnutrition.data.repository

import com.sportsnutrition.data.api.ApiService
import com.sportsnutrition.data.local.dao.RestaurantDao
import com.sportsnutrition.data.models.toDomain
import com.sportsnutrition.data.models.toEntity
import com.sportsnutrition.data.local.entities.toDomain
import com.sportsnutrition.domain.models.Restaurant
import com.sportsnutrition.domain.repository.RestaurantRepository
import com.sportsnutrition.utils.Constants
import com.sportsnutrition.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class RestaurantRepositoryImpl @Inject constructor(
    private val api: ApiService,
    private val dao: RestaurantDao,
) : RestaurantRepository {

    override fun getRestaurants(
        search: String?,
        city: String?,
        cuisine: String?,
        page: Int,
    ): Flow<Result<List<Restaurant>>> = flow {
        emit(Result.Loading)

        // Emit cached data immediately
        val cachedAt = dao.getLastCacheTime()
        val isCacheValid = cachedAt != null &&
            System.currentTimeMillis() - cachedAt < TimeUnit.HOURS.toMillis(Constants.CACHE_EXPIRY_HOURS)

        if (isCacheValid && search == null && city == null && cuisine == null) {
            dao.getAllRestaurants().collect { entities ->
                if (entities.isNotEmpty()) emit(Result.Success(entities.map { it.toDomain() }))
            }
        }

        // Fetch fresh from network
        runCatching {
            val response = api.getRestaurants(search, city, cuisine, page)
            val body = response.body()
            if (response.isSuccessful && body?.success == true) {
                val restaurants = body.data?.restaurants ?: emptyList()
                if (search == null && city == null && cuisine == null && page == 1) {
                    dao.deleteAll()
                    dao.insertAll(restaurants.map { it.toEntity() })
                }
                emit(Result.Success(restaurants.map { it.toDomain() }))
            } else {
                emit(Result.Error(body?.toString() ?: "Ophalen restaurants mislukt"))
            }
        }.onFailure { emit(Result.Error(it.message ?: "Netwerkfout")) }
    }

    override suspend fun getRestaurantById(id: String): Result<Restaurant> =
        runCatching {
            val cached = dao.getRestaurantById(id)
            if (cached != null) return@runCatching Result.Success(cached.toDomain())

            val response = api.getRestaurantById(id)
            val body = response.body()
            if (response.isSuccessful && body?.success == true && body.data != null) {
                dao.insert(body.data.toEntity())
                Result.Success(body.data.toDomain())
            } else {
                Result.Error("Restaurant niet gevonden")
            }
        }.getOrElse { Result.Error(it.message ?: "Netwerkfout") }
}
