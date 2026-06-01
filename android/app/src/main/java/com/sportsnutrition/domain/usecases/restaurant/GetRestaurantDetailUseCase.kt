package com.sportsnutrition.domain.usecases.restaurant

import com.sportsnutrition.domain.models.Restaurant
import com.sportsnutrition.domain.repository.RestaurantRepository
import com.sportsnutrition.utils.Result
import javax.inject.Inject

class GetRestaurantDetailUseCase @Inject constructor(private val repository: RestaurantRepository) {

    suspend operator fun invoke(id: String): Result<Restaurant> = repository.getRestaurantById(id)
}
