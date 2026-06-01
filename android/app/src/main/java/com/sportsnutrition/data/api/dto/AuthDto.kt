package com.sportsnutrition.data.api.dto

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
)

data class LoginRequest(
    val email: String,
    val password: String,
)

data class AuthResponse(
    val success: Boolean,
    val data: AuthData?,
    val message: String? = null,
)

data class AuthData(
    val token: String,
    val user: UserDto,
)

data class UserDto(
    @SerializedName("_id") val id: String,
    val name: String,
    val email: String,
    val age: Int?,
    val weight: Double?,
    val height: Double?,
    val gender: String?,
    val activityLevel: String?,
    val goal: String?,
    val role: String?,
    val profileImageUrl: String?,
)

data class ProfileResponse(
    val success: Boolean,
    val data: ProfileData?,
)

data class ProfileData(
    val user: UserDto,
    val nutrition: NutritionDto?,
)

data class NutritionDto(
    val bmr: Int,
    val tdee: Int,
    val goalCalories: Int,
    val proteinGoal: Int,
)

data class UpdateProfileRequest(
    val name: String? = null,
    val age: Int? = null,
    val weight: Double? = null,
    val height: Double? = null,
    val gender: String? = null,
    val activityLevel: String? = null,
    val goal: String? = null,
)
