package com.sportsnutrition.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sportsnutrition.domain.models.Restaurant

@Entity(tableName = "restaurants")
data class RestaurantEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String?,
    val address: String,
    val city: String,
    val postalCode: String?,
    val imageUrl: String?,
    val cuisineTypeJson: String, // stored as JSON string
    val cachedAt: Long = System.currentTimeMillis(),
)

fun RestaurantEntity.toDomain() = Restaurant(
    id = id,
    name = name,
    description = description,
    address = address,
    city = city,
    postalCode = postalCode,
    imageUrl = imageUrl,
    cuisineType = com.google.gson.Gson().fromJson(cuisineTypeJson, Array<String>::class.java).toList(),
)
