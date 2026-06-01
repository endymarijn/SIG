package com.sportsnutrition.domain.usecases.auth

import com.sportsnutrition.domain.models.User
import com.sportsnutrition.domain.repository.AuthRepository
import com.sportsnutrition.utils.Result
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val repository: AuthRepository) {

    suspend operator fun invoke(email: String, password: String): Result<User> {
        if (email.isBlank()) return Result.Error("Email mag niet leeg zijn")
        if (password.length < 8) return Result.Error("Wachtwoord moet minimaal 8 tekens bevatten")
        return repository.login(email.trim(), password)
    }
}
