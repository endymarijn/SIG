package com.sportsnutrition.data.models

import com.google.gson.Gson
import com.sportsnutrition.data.api.dto.*
import com.sportsnutrition.data.local.entities.DishEntity
import com.sportsnutrition.data.local.entities.RestaurantEntity
import com.sportsnutrition.domain.models.*
import com.sportsnutrition.utils.toNutritionTagOrNull

private val gson = Gson()

fun UserDto.toDomain() = User(
    id = id,
    name = name,
    email = email,
    age = age,
    weight = weight,
    height = height,
    gender = gender?.uppercase()?.let { runCatching { Gender.valueOf(it) }.getOrNull() },
    activityLevel = activityLevel?.let { v -> ActivityLevel.entries.firstOrNull { it.apiValue == v } } ?: ActivityLevel.MODERATE,
    goal = goal?.let { v -> UserGoal.entries.firstOrNull { it.apiValue == v } } ?: UserGoal.MAINTENANCE,
    role = if (role == "admin") UserRole.ADMIN else UserRole.USER,
    profileImageUrl = profileImageUrl,
)

fun NutritionDto.toDomain() = NutritionGoals(bmr, tdee, goalCalories, proteinGoal)

fun RestaurantDto.toDomain() = Restaurant(
    id = id,
    name = name,
    description = description,
    address = address,
    city = city,
    postalCode = postalCode,
    imageUrl = imageUrl,
    cuisineType = cuisineType ?: emptyList(),
    openingHours = openingHours?.map { oh ->
        OpeningHours(oh.day, oh.open ?: "", oh.close ?: "", oh.isClosed ?: false)
    } ?: emptyList(),
    phoneNumber = phoneNumber,
    website = website,
)

fun RestaurantDto.toEntity() = RestaurantEntity(
    id = id,
    name = name,
    description = description,
    address = address,
    city = city,
    postalCode = postalCode,
    imageUrl = imageUrl,
    cuisineTypeJson = gson.toJson(cuisineType ?: emptyList<String>()),
)

fun DishDto.toDomain() = Dish(
    id = id,
    restaurantId = when (val r = restaurantId) {
        is RestaurantRefDto -> r.id
        else -> ""
    },
    restaurantName = (restaurantId as? RestaurantRefDto)?.name,
    name = name,
    description = description,
    imageUrl = imageUrl,
    category = category,
    price = price,
    servingSize = servingSize ?: 100,
    calories = calories,
    protein = protein,
    carbs = carbs,
    fat = fat,
    fiber = fiber ?: 0.0,
    sugar = sugar ?: 0.0,
    sodium = sodium ?: 0.0,
    tags = tags?.mapNotNull { it.toNutritionTagOrNull() } ?: emptyList(),
)

fun DishDto.toEntity() = DishEntity(
    id = id,
    restaurantId = when (val r = restaurantId) {
        is RestaurantRefDto -> r.id
        else -> ""
    },
    restaurantName = (restaurantId as? RestaurantRefDto)?.name,
    name = name,
    description = description,
    imageUrl = imageUrl,
    category = category,
    price = price,
    servingSize = servingSize ?: 100,
    calories = calories,
    protein = protein,
    carbs = carbs,
    fat = fat,
    fiber = fiber ?: 0.0,
    sugar = sugar ?: 0.0,
    sodium = sodium ?: 0.0,
    tagsJson = gson.toJson(tags ?: emptyList<String>()),
)
