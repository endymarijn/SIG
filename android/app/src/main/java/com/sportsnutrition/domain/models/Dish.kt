package com.sportsnutrition.domain.models

data class Dish(
    val id: String,
    val restaurantId: String,
    val restaurantName: String? = null,
    val name: String,
    val description: String? = null,
    val imageUrl: String? = null,
    val category: String? = null,
    val price: Double? = null,
    val servingSize: Int = 100,
    val calories: Double,
    val protein: Double,
    val carbs: Double,
    val fat: Double,
    val fiber: Double = 0.0,
    val sugar: Double = 0.0,
    val sodium: Double = 0.0,
    val tags: List<NutritionTag> = emptyList(),
)

enum class NutritionTag(val displayName: String, val emoji: String) {
    HOOG_EIWIT("Hoog Eiwit", "💪"),
    BULK_FRIENDLY("Bulk Friendly", "🏋️"),
    CUT_FRIENDLY("Cut Friendly", "✂️"),
    KETO_FRIENDLY("Keto Friendly", "🥑"),
    CALORIEARM("Caloriearm", "🌿"),
    VEZELRIJK("Vezelrijk", "🌾"),
    VEGETARISCH("Vegetarisch", "🥗"),
    VEGAN("Vegan", "🌱"),
    GLUTENVRIJ("Glutenvrij", "🌾"),
    LACTOSEVRIJ("Lactosevrij", "🥛"),
}
