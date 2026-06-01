package com.sportsnutrition.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sportsnutrition.domain.models.User
import com.sportsnutrition.domain.repository.AuthRepository
import com.sportsnutrition.domain.usecases.auth.LoginUseCase
import com.sportsnutrition.domain.usecases.auth.RegisterUseCase
import com.sportsnutrition.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val error: String? = null,
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState

    val isLoggedIn: StateFlow<Boolean?> = authRepository.getAuthToken()
        .map { it != null }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState(isLoading = true)
            when (val result = loginUseCase(email, password)) {
                is Result.Success -> _uiState.value = AuthUiState(user = result.data)
                is Result.Error -> _uiState.value = AuthUiState(error = result.message)
                else -> Unit
            }
        }
    }

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState(isLoading = true)
            when (val result = registerUseCase(name, email, password)) {
                is Result.Success -> _uiState.value = AuthUiState(user = result.data)
                is Result.Error -> _uiState.value = AuthUiState(error = result.message)
                else -> Unit
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _uiState.value = AuthUiState()
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
