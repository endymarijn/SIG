package com.sportsnutrition.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sportsnutrition.presentation.screens.auth.LoginScreen
import com.sportsnutrition.presentation.screens.auth.RegisterScreen
import com.sportsnutrition.presentation.screens.dish.DishDetailScreen
import com.sportsnutrition.presentation.screens.favorites.FavoritesScreen
import com.sportsnutrition.presentation.screens.home.HomeScreen
import com.sportsnutrition.presentation.screens.profile.ProfileScreen
import com.sportsnutrition.presentation.screens.restaurant.RestaurantDetailScreen
import com.sportsnutrition.presentation.screens.restaurant.RestaurantListScreen
import com.sportsnutrition.presentation.viewmodels.AuthViewModel

@Composable
fun NavGraph(navController: NavHostController = rememberNavController()) {
    val authViewModel: AuthViewModel = hiltViewModel()
    val isLoggedIn by authViewModel.isLoggedIn.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn == true) Routes.Home.route else Routes.Login.route,
    ) {
        composable(Routes.Login.route) {
            LoginScreen(
                onLoginSuccess = { navController.navigate(Routes.Home.route) { popUpTo(0) } },
                onRegisterClick = { navController.navigate(Routes.Register.route) },
            )
        }

        composable(Routes.Register.route) {
            RegisterScreen(
                onRegisterSuccess = { navController.navigate(Routes.Home.route) { popUpTo(0) } },
                onLoginClick = { navController.popBackStack() },
            )
        }

        composable(Routes.Home.route) {
            HomeScreen(navController = navController)
        }

        composable(Routes.RestaurantList.route) {
            RestaurantListScreen(
                onRestaurantClick = { id -> navController.navigate(Routes.RestaurantDetail.createRoute(id)) },
                onBack = { navController.popBackStack() },
            )
        }

        composable(
            route = Routes.RestaurantDetail.route,
            arguments = listOf(navArgument("restaurantId") { type = NavType.StringType }),
        ) { backStack ->
            RestaurantDetailScreen(
                restaurantId = backStack.arguments?.getString("restaurantId") ?: "",
                onDishClick = { id -> navController.navigate(Routes.DishDetail.createRoute(id)) },
                onBack = { navController.popBackStack() },
            )
        }

        composable(
            route = Routes.DishDetail.route,
            arguments = listOf(navArgument("dishId") { type = NavType.StringType }),
        ) { backStack ->
            DishDetailScreen(
                dishId = backStack.arguments?.getString("dishId") ?: "",
                onBack = { navController.popBackStack() },
            )
        }

        composable(Routes.Profile.route) {
            ProfileScreen(
                onLogout = { navController.navigate(Routes.Login.route) { popUpTo(0) } },
                onBack = { navController.popBackStack() },
            )
        }

        composable(Routes.Favorites.route) {
            FavoritesScreen(
                onRestaurantClick = { id -> navController.navigate(Routes.RestaurantDetail.createRoute(id)) },
                onDishClick = { id -> navController.navigate(Routes.DishDetail.createRoute(id)) },
                onBack = { navController.popBackStack() },
            )
        }
    }
}
