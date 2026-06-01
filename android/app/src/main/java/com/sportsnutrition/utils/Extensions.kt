package com.sportsnutrition.utils

import retrofit2.Response

fun <T> Response<T>.toResult(): Result<T> {
    return if (isSuccessful && body() != null) {
        Result.Success(body()!!)
    } else {
        Result.Error(message() ?: "Unknown error", code())
    }
}

fun Double.formatNutrient(): String = if (this % 1.0 == 0.0) toInt().toString() else "%.1f".format(this)

fun Double.toCaloriesString(): String = "${toInt()} kcal"

fun String.toNutritionTagOrNull(): com.sportsnutrition.domain.models.NutritionTag? =
    com.sportsnutrition.domain.models.NutritionTag.entries.firstOrNull {
        it.name.lowercase() == this.lowercase().replace(" ", "_")
    }
