package com.sportsnutrition.domain.usecases.auth

import com.sportsnutrition.domain.models.User
import com.sportsnutrition.domain.repository.AuthRepository
import com.sportsnutrition.utils.Result
import javax.inject.Inject

class RegisterUseCase @Inject constructor(private val repository: AuthRepository) {

    suspend operator fun invoke(name: String, email: String, password: String): Result<User> {
        if (name.isBlank()) return Result.Error("Naam mag niet leeg zijn")
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) return Result.Error("Ongeldig e-mailadres")
        if (password.length < 8) return Result.Error("Wachtwoord moet minimaal 8 tekens bevatten")
        return repository.register(name.trim(), email.trim().lowercase(), password)
    }
}
