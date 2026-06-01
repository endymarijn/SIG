package com.sportsnutrition.domain.models

data class Restaurant(
    val id: String,
    val name: String,
    val description: String? = null,
    val address: String,
    val city: String,
    val postalCode: String? = null,
    val imageUrl: String? = null,
    val cuisineType: List<String> = emptyList(),
    val openingHours: List<OpeningHours> = emptyList(),
    val phoneNumber: String? = null,
    val website: String? = null,
)

data class OpeningHours(
    val day: String,
    val open: String,
    val close: String,
    val isClosed: Boolean = false,
)
