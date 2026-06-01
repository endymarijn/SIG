package com.sportsnutrition.domain.models

data class Favorite(
    val id: String,
    val type: FavoriteType,
    val itemId: String,
    val restaurant: Restaurant? = null,
    val dish: Dish? = null,
)

enum class FavoriteType { RESTAURANT, DISH }
