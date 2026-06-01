package com.sportsnutrition.domain.models

data class User(
    val id: String,
    val name: String,
    val email: String,
    val age: Int? = null,
    val weight: Double? = null,
    val height: Double? = null,
    val gender: Gender? = null,
    val activityLevel: ActivityLevel = ActivityLevel.MODERATE,
    val goal: UserGoal = UserGoal.MAINTENANCE,
    val role: UserRole = UserRole.USER,
    val profileImageUrl: String? = null,
)

enum class Gender { MALE, FEMALE, OTHER }

enum class ActivityLevel(val apiValue: String, val displayName: String) {
    SEDENTARY("sedentary", "Zittend"),
    LIGHT("light", "Licht actief"),
    MODERATE("moderate", "Matig actief"),
    ACTIVE("active", "Actief"),
    VERY_ACTIVE("very_active", "Zeer actief"),
}

enum class UserGoal(val apiValue: String, val displayName: String) {
    LOSE_WEIGHT("lose_weight", "Afvallen"),
    MAINTENANCE("maintenance", "Onderhoud"),
    GAIN_MUSCLE("gain_muscle", "Spiermassa opbouwen"),
}

enum class UserRole { USER, ADMIN }

data class NutritionGoals(
    val bmr: Int,
    val tdee: Int,
    val goalCalories: Int,
    val proteinGoal: Int,
)
