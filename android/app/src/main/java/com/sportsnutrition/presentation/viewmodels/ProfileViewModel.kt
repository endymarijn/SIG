package com.sportsnutrition.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sportsnutrition.domain.models.NutritionGoals
import com.sportsnutrition.domain.models.User
import com.sportsnutrition.domain.repository.AuthRepository
import com.sportsnutrition.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val user: User? = null,
    val nutritionGoals: NutritionGoals? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val updateSuccess: Boolean = false,
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState

    init { loadProfile() }

    fun loadProfile() {
        viewModelScope.launch {
            _uiState.value = ProfileUiState(isLoading = true)
            when (val result = authRepository.getMe()) {
                is Result.Success -> _uiState.value = ProfileUiState(
                    user = result.data.first,
                    nutritionGoals = result.data.second,
                )
                is Result.Error -> _uiState.value = ProfileUiState(error = result.message)
                else -> Unit
            }
        }
    }

    fun logout() {
        viewModelScope.launch { authRepository.logout() }
    }
}
