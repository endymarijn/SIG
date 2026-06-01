package com.sportsnutrition.domain.repository

import com.sportsnutrition.domain.models.Restaurant
import com.sportsnutrition.utils.Result
import kotlinx.coroutines.flow.Flow

interface RestaurantRepository {
    fun getRestaurants(
        search: String? = null,
        city: String? = null,
        cuisine: String? = null,
        page: Int = 1,
    ): Flow<Result<List<Restaurant>>>

    suspend fun getRestaurantById(id: String): Result<Restaurant>
}
