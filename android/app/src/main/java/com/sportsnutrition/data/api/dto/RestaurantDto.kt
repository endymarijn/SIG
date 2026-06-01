package com.sportsnutrition.data.api.dto

import com.google.gson.annotations.SerializedName

data class RestaurantsResponse(
    val success: Boolean,
    val data: RestaurantsData?,
)

data class RestaurantsData(
    val restaurants: List<RestaurantDto>,
    val pagination: PaginationDto,
)

data class RestaurantResponse(
    val success: Boolean,
    val data: RestaurantDto?,
)

data class RestaurantDto(
    @SerializedName("_id") val id: String,
    val name: String,
    val description: String?,
    val address: String,
    val city: String,
    val postalCode: String?,
    val imageUrl: String?,
    val cuisineType: List<String>?,
    val openingHours: List<OpeningHoursDto>?,
    val phoneNumber: String?,
    val website: String?,
)

data class OpeningHoursDto(
    val day: String,
    val open: String?,
    val close: String?,
    val isClosed: Boolean?,
)

data class PaginationDto(
    val page: Int,
    val limit: Int,
    val total: Int,
    val pages: Int,
)
