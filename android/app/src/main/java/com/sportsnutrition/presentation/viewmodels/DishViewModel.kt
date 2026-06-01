package com.sportsnutrition.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sportsnutrition.domain.models.Dish
import com.sportsnutrition.domain.usecases.dish.GetDishDetailUseCase
import com.sportsnutrition.domain.usecases.dish.GetDishesUseCase
import com.sportsnutrition.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DishListUiState(
    val dishes: List<Dish> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)

data class DishDetailUiState(
    val dish: Dish? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)

@HiltViewModel
class DishViewModel @Inject constructor(
    private val getDishesUseCase: GetDishesUseCase,
    private val getDishDetailUseCase: GetDishDetailUseCase,
) : ViewModel() {

    private val _listState = MutableStateFlow(DishListUiState())
    val listState: StateFlow<DishListUiState> = _listState

    private val _detailState = MutableStateFlow(DishDetailUiState())
    val detailState: StateFlow<DishDetailUiState> = _detailState

    fun loadDishesForRestaurant(restaurantId: String) {
        getDishesUseCase(restaurantId).onEach { result ->
            _listState.value = when (result) {
                is Result.Loading -> _listState.value.copy(isLoading = true, error = null)
                is Result.Success -> DishListUiState(dishes = result.data, isLoading = false)
                is Result.Error -> _listState.value.copy(isLoading = false, error = result.message)
            }
        }.launchIn(viewModelScope)
    }

    fun loadDishDetail(id: String) {
        viewModelScope.launch {
            _detailState.value = DishDetailUiState(isLoading = true)
            _detailState.value = when (val result = getDishDetailUseCase(id)) {
                is Result.Success -> DishDetailUiState(dish = result.data)
                is Result.Error -> DishDetailUiState(error = result.message)
                else -> DishDetailUiState(isLoading = true)
            }
        }
    }
}
