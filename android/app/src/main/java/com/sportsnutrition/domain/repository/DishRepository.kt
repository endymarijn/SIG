package com.sportsnutrition.domain.repository

import com.sportsnutrition.domain.models.Dish
import com.sportsnutrition.utils.Result
import kotlinx.coroutines.flow.Flow

interface DishRepository {
    fun getDishesByRestaurant(restaurantId: String): Flow<Result<List<Dish>>>
    fun searchDishes(
        query: String? = null,
        tags: List<String> = emptyList(),
        minProtein: Double? = null,
        maxCalories: Double? = null,
    ): Flow<Result<List<Dish>>>

    suspend fun getDishById(id: String): Result<Dish>
}
