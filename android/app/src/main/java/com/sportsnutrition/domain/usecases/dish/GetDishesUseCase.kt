package com.sportsnutrition.domain.usecases.dish

import com.sportsnutrition.domain.models.Dish
import com.sportsnutrition.domain.repository.DishRepository
import com.sportsnutrition.utils.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDishesUseCase @Inject constructor(private val repository: DishRepository) {

    operator fun invoke(restaurantId: String): Flow<Result<List<Dish>>> =
        repository.getDishesByRestaurant(restaurantId)
}
