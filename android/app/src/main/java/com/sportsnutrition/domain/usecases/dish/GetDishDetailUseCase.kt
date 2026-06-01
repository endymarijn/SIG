package com.sportsnutrition.domain.usecases.dish

import com.sportsnutrition.domain.models.Dish
import com.sportsnutrition.domain.repository.DishRepository
import com.sportsnutrition.utils.Result
import javax.inject.Inject

class GetDishDetailUseCase @Inject constructor(private val repository: DishRepository) {

    suspend operator fun invoke(id: String): Result<Dish> = repository.getDishById(id)
}
