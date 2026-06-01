package com.sportsnutrition.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sportsnutrition.domain.models.Dish
import com.sportsnutrition.utils.toNutritionTagOrNull

@Entity(tableName = "dishes")
data class DishEntity(
    @PrimaryKey val id: String,
    val restaurantId: String,
    val restaurantName: String?,
    val name: String,
    val description: String?,
    val imageUrl: String?,
    val category: String?,
    val price: Double?,
    val servingSize: Int,
    val calories: Double,
    val protein: Double,
    val carbs: Double,
    val fat: Double,
    val fiber: Double,
    val sugar: Double,
    val sodium: Double,
    val tagsJson: String,
    val cachedAt: Long = System.currentTimeMillis(),
)

fun DishEntity.toDomain() = Dish(
    id = id,
    restaurantId = restaurantId,
    restaurantName = restaurantName,
    name = name,
    description = description,
    imageUrl = imageUrl,
    category = category,
    price = price,
    servingSize = servingSize,
    calories = calories,
    protein = protein,
    carbs = carbs,
    fat = fat,
    fiber = fiber,
    sugar = sugar,
    sodium = sodium,
    tags = com.google.gson.Gson().fromJson(tagsJson, Array<String>::class.java)
        .mapNotNull { it.toNutritionTagOrNull() },
)
