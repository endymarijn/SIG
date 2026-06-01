package com.sportsnutrition.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sportsnutrition.domain.models.Restaurant
import com.sportsnutrition.domain.usecases.restaurant.GetRestaurantDetailUseCase
import com.sportsnutrition.domain.usecases.restaurant.GetRestaurantsUseCase
import com.sportsnutrition.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RestaurantListUiState(
    val restaurants: List<Restaurant> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)

data class RestaurantDetailUiState(
    val restaurant: Restaurant? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)

@OptIn(FlowPreview::class)
@HiltViewModel
class RestaurantViewModel @Inject constructor(
    private val getRestaurantsUseCase: GetRestaurantsUseCase,
    private val getRestaurantDetailUseCase: GetRestaurantDetailUseCase,
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _cityFilter = MutableStateFlow<String?>(null)
    private val _cuisineFilter = MutableStateFlow<String?>(null)

    private val _listState = MutableStateFlow(RestaurantListUiState())
    val listState: StateFlow<RestaurantListUiState> = _listState

    private val _detailState = MutableStateFlow(RestaurantDetailUiState())
    val detailState: StateFlow<RestaurantDetailUiState> = _detailState

    init {
        combine(_searchQuery.debounce(300), _cityFilter, _cuisineFilter) { q, city, cuisine ->
            Triple(q.ifBlank { null }, city, cuisine)
        }.onEach { (q, city, cuisine) ->
            loadRestaurants(q, city, cuisine)
        }.launchIn(viewModelScope)
    }

    private fun loadRestaurants(search: String?, city: String?, cuisine: String?) {
        getRestaurantsUseCase(search, city, cuisine).onEach { result ->
            _listState.value = when (result) {
                is Result.Loading -> _listState.value.copy(isLoading = true, error = null)
                is Result.Success -> RestaurantListUiState(restaurants = result.data, isLoading = false)
                is Result.Error -> _listState.value.copy(isLoading = false, error = result.message)
            }
        }.launchIn(viewModelScope)
    }

    fun loadRestaurantDetail(id: String) {
        viewModelScope.launch {
            _detailState.value = RestaurantDetailUiState(isLoading = true)
            _detailState.value = when (val result = getRestaurantDetailUseCase(id)) {
                is Result.Success -> RestaurantDetailUiState(restaurant = result.data)
                is Result.Error -> RestaurantDetailUiState(error = result.message)
                else -> RestaurantDetailUiState(isLoading = true)
            }
        }
    }

    fun updateSearch(query: String) { _searchQuery.value = query }
    fun updateCityFilter(city: String?) { _cityFilter.value = city }
    fun updateCuisineFilter(cuisine: String?) { _cuisineFilter.value = cuisine }
}
