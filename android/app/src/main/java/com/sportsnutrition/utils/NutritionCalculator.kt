package com.sportsnutrition.utils

import com.sportsnutrition.domain.models.ActivityLevel
import com.sportsnutrition.domain.models.Gender
import com.sportsnutrition.domain.models.UserGoal

object NutritionCalculator {

    fun calculateBMR(weight: Double, height: Double, age: Int, gender: Gender): Double = when (gender) {
        Gender.MALE -> 88.362 + 13.397 * weight + 4.799 * height - 5.677 * age
        Gender.FEMALE -> 447.593 + 9.247 * weight + 3.098 * height - 4.330 * age
        Gender.OTHER -> (calculateBMR(weight, height, age, Gender.MALE) + calculateBMR(weight, height, age, Gender.FEMALE)) / 2
    }

    fun calculateTDEE(bmr: Double, activityLevel: ActivityLevel): Int {
        val multiplier = when (activityLevel) {
            ActivityLevel.SEDENTARY -> 1.2
            ActivityLevel.LIGHT -> 1.375
            ActivityLevel.MODERATE -> 1.55
            ActivityLevel.ACTIVE -> 1.725
            ActivityLevel.VERY_ACTIVE -> 1.9
        }
        return (bmr * multiplier).toInt()
    }

    fun calculateGoalCalories(tdee: Int, goal: UserGoal): Int = when (goal) {
        UserGoal.LOSE_WEIGHT -> tdee - 500
        UserGoal.MAINTENANCE -> tdee
        UserGoal.GAIN_MUSCLE -> tdee + 300
    }

    fun calculateProteinGoal(weight: Double, goal: UserGoal): Int {
        val multiplier = when (goal) {
            UserGoal.LOSE_WEIGHT -> 2.2
            UserGoal.MAINTENANCE -> 1.8
            UserGoal.GAIN_MUSCLE -> 2.4
        }
        return (weight * multiplier).toInt()
    }

    fun macroPercentages(protein: Double, carbs: Double, fat: Double): Triple<Float, Float, Float> {
        val proteinCal = protein * 4
        val carbsCal = carbs * 4
        val fatCal = fat * 9
        val total = proteinCal + carbsCal + fatCal
        if (total == 0.0) return Triple(0f, 0f, 0f)
        return Triple(
            (proteinCal / total * 100).toFloat(),
            (carbsCal / total * 100).toFloat(),
            (fatCal / total * 100).toFloat(),
        )
    }
}
