package com.sportsnutrition.data.repository

import com.sportsnutrition.data.api.ApiService
import com.sportsnutrition.data.local.dao.DishDao
import com.sportsnutrition.data.local.entities.toDomain
import com.sportsnutrition.data.models.toDomain
import com.sportsnutrition.data.models.toEntity
import com.sportsnutrition.domain.models.Dish
import com.sportsnutrition.domain.repository.DishRepository
import com.sportsnutrition.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DishRepositoryImpl @Inject constructor(
    private val api: ApiService,
    private val dao: DishDao,
) : DishRepository {

    override fun getDishesByRestaurant(restaurantId: String): Flow<Result<List<Dish>>> = flow {
        emit(Result.Loading)

        dao.getDishesByRestaurant(restaurantId).collect { cached ->
            if (cached.isNotEmpty()) emit(Result.Success(cached.map { it.toDomain() }))
        }

        runCatching {
            val response = api.getDishes(restaurantId = restaurantId)
            val body = response.body()
            if (response.isSuccessful && body?.success == true) {
                val dishes = body.data?.dishes ?: emptyList()
                dao.deleteByRestaurant(restaurantId)
                dao.insertAll(dishes.map { it.toEntity() })
                emit(Result.Success(dishes.map { it.toDomain() }))
            } else {
                emit(Result.Error("Ophalen gerechten mislukt"))
            }
        }.onFailure { emit(Result.Error(it.message ?: "Netwerkfout")) }
    }

    override fun searchDishes(
        query: String?,
        tags: List<String>,
        minProtein: Double?,
        maxCalories: Double?,
    ): Flow<Result<List<Dish>>> = flow {
        emit(Result.Loading)
        runCatching {
            val response = api.getDishes(
                search = query,
                tags = tags.joinToString(",").ifBlank { null },
                minProtein = minProtein,
                maxCalories = maxCalories,
            )
            val body = response.body()
            if (response.isSuccessful && body?.success == true) {
                emit(Result.Success(body.data?.dishes?.map { it.toDomain() } ?: emptyList()))
            } else {
                emit(Result.Error("Zoeken mislukt"))
            }
        }.onFailure { emit(Result.Error(it.message ?: "Netwerkfout")) }
    }

    override suspend fun getDishById(id: String): Result<Dish> =
        runCatching {
            val cached = dao.getDishById(id)
            if (cached != null) return@runCatching Result.Success(cached.toDomain())

            val response = api.getDishById(id)
            val body = response.body()
            if (response.isSuccessful && body?.success == true && body.data != null) {
                dao.insert(body.data.toEntity())
                Result.Success(body.data.toDomain())
            } else {
                Result.Error("Gerecht niet gevonden")
            }
        }.getOrElse { Result.Error(it.message ?: "Netwerkfout") }
}
