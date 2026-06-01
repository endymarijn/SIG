package com.sportsnutrition.presentation.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.sportsnutrition.presentation.navigation.Routes
import com.sportsnutrition.presentation.theme.Dimensions
import com.sportsnutrition.presentation.viewmodels.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    profileViewModel: ProfileViewModel = hiltViewModel(),
) {
    val profileState by profileViewModel.uiState.collectAsStateWithLifecycle()
    var selectedTab by remember { mutableIntStateOf(0) }

    val tabs = listOf(
        Triple("Home", Icons.Default.Home, Routes.Home.route),
        Triple("Restaurants", Icons.Default.Restaurant, Routes.RestaurantList.route),
        Triple("Favorieten", Icons.Default.Favorite, Routes.Favorites.route),
        Triple("Profiel", Icons.Default.Person, Routes.Profile.route),
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sports Nutrition") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                ),
            )
        },
        bottomBar = {
            NavigationBar {
                tabs.forEachIndexed { index, (label, icon, _) ->
                    NavigationBarItem(
                        icon = { Icon(icon, contentDescription = label) },
                        label = { Text(label, style = MaterialTheme.typography.labelSmall) },
                        selected = selectedTab == index,
                        onClick = {
                            selectedTab = index
                            if (index > 0) navController.navigate(tabs[index].third)
                        },
                    )
                }
            }
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(Dimensions.spacingMd),
            verticalArrangement = Arrangement.spacedBy(Dimensions.spacingMd),
        ) {
            // Welkomstkaart
            val user = profileState.user
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                shape = MaterialTheme.shapes.extraLarge,
            ) {
                Column(modifier = Modifier.padding(Dimensions.spacingLg)) {
                    Text(
                        text = if (user != null) "Hoi, ${user.name.substringBefore(' ')}! 👋" else "Welkom! 👋",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                    if (user?.goal != null) {
                        Spacer(Modifier.height(Dimensions.spacingXs))
                        Text(
                            text = "Doel: ${user.goal.displayName}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                        )
                    }
                }
            }

            // Calorie doelen kaart
            val goals = profileState.nutritionGoals
            if (goals != null) {
                Card(modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.large) {
                    Column(modifier = Modifier.padding(Dimensions.spacingMd)) {
                        Text("Jouw dagdoelen", style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(Dimensions.spacingMd))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                        ) {
                            GoalItem("${goals.goalCalories}", "kcal", MaterialTheme.colorScheme.error)
                            GoalItem("${goals.proteinGoal}g", "eiwit", MaterialTheme.colorScheme.primary)
                            GoalItem("${goals.tdee}", "TDEE", MaterialTheme.colorScheme.tertiary)
                        }
                    }
                }
            }

            // Snelknoppen
            Text("Verkennen", style = MaterialTheme.typography.titleMedium)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Dimensions.spacingMd),
            ) {
                QuickActionCard(
                    emoji = "🍽️",
                    title = "Restaurants",
                    subtitle = "Zoek restaurants",
                    modifier = Modifier.weight(1f),
                    onClick = { navController.navigate(Routes.RestaurantList.route) },
                )
                QuickActionCard(
                    emoji = "⭐",
                    title = "Favorieten",
                    subtitle = "Opgeslagen items",
                    modifier = Modifier.weight(1f),
                    onClick = { navController.navigate(Routes.Favorites.route) },
                )
            }
        }
    }
}

@Composable
private fun GoalItem(value: String, label: String, color: androidx.compose.ui.graphics.Color) {
    Column(horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.titleLarge, color = color)
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun QuickActionCard(emoji: String, title: String, subtitle: String, modifier: Modifier, onClick: () -> Unit) {
    Card(
        modifier = modifier,
        onClick = onClick,
        shape = MaterialTheme.shapes.large,
    ) {
        Column(modifier = Modifier.padding(Dimensions.spacingMd)) {
            Text(emoji, style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(Dimensions.spacingXs))
            Text(title, style = MaterialTheme.typography.titleSmall)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
