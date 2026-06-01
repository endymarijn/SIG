package com.sportsnutrition.domain.usecases.restaurant

import com.sportsnutrition.domain.models.Restaurant
import com.sportsnutrition.domain.repository.RestaurantRepository
import com.sportsnutrition.utils.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRestaurantsUseCase @Inject constructor(private val repository: RestaurantRepository) {

    operator fun invoke(
        search: String? = null,
        city: String? = null,
        cuisine: String? = null,
        page: Int = 1,
    ): Flow<Result<List<Restaurant>>> = repository.getRestaurants(search, city, cuisine, page)
}
