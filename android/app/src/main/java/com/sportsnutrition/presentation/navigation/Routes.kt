package com.sportsnutrition.presentation.navigation

sealed class Routes(val route: String) {
    data object Login : Routes("login")
    data object Register : Routes("register")
    data object Home : Routes("home")
    data object RestaurantList : Routes("restaurants")
    data object RestaurantDetail : Routes("restaurant/{restaurantId}") {
        fun createRoute(id: String) = "restaurant/$id"
    }
    data object DishDetail : Routes("dish/{dishId}") {
        fun createRoute(id: String) = "dish/$id"
    }
    data object Profile : Routes("profile")
    data object Favorites : Routes("favorites")
}
