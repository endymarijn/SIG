package com.sportsnutrition.data.api.dto

import com.google.gson.annotations.SerializedName

data class DishesResponse(
    val success: Boolean,
    val data: DishesData?,
)

data class DishesData(
    val dishes: List<DishDto>,
    val pagination: PaginationDto,
)

data class DishResponse(
    val success: Boolean,
    val data: DishDto?,
)

data class DishDto(
    @SerializedName("_id") val id: String,
    val restaurantId: RestaurantRefDto?,
    val name: String,
    val description: String?,
    val imageUrl: String?,
    val category: String?,
    val price: Double?,
    val servingSize: Int?,
    val calories: Double,
    val protein: Double,
    val carbs: Double,
    val fat: Double,
    val fiber: Double?,
    val sugar: Double?,
    val sodium: Double?,
    val tags: List<String>?,
)

data class RestaurantRefDto(
    @SerializedName("_id") val id: String,
    val name: String?,
    val city: String?,
)

data class FavoritesResponse(
    val success: Boolean,
    val data: List<FavoriteDto>?,
)

data class FavoriteDto(
    @SerializedName("_id") val id: String,
    val type: String,
    val itemId: Any?,
)

data class AddFavoriteRequest(
    val type: String,
    val itemId: String,
)
