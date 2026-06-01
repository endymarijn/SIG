package com.sportsnutrition.presentation.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sportsnutrition.presentation.components.ErrorView
import com.sportsnutrition.presentation.components.LoadingIndicator
import com.sportsnutrition.presentation.theme.CalorieColor
import com.sportsnutrition.presentation.theme.Dimensions
import com.sportsnutrition.presentation.theme.ProteinColor
import com.sportsnutrition.presentation.viewmodels.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onLogout: () -> Unit,
    onBack: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showLogoutDialog by remember { mutableStateOf(false) }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Uitloggen") },
            text = { Text("Weet je zeker dat je wilt uitloggen?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.logout()
                    onLogout()
                }) { Text("Uitloggen") }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) { Text("Annuleren") }
            },
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mijn Profiel") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Terug")
                    }
                },
                actions = {
                    IconButton(onClick = { showLogoutDialog = true }) {
                        Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Uitloggen")
                    }
                },
            )
        }
    ) { padding ->
        when {
            uiState.isLoading -> LoadingIndicator()
            uiState.error != null -> ErrorView(message = uiState.error!!, onRetry = { viewModel.loadProfile() })
            else -> {
                val user = uiState.user ?: return@Scaffold
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .verticalScroll(rememberScrollState())
                        .padding(Dimensions.spacingMd),
                    verticalArrangement = Arrangement.spacedBy(Dimensions.spacingMd),
                ) {
                    // Profile header
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                        shape = MaterialTheme.shapes.extraLarge,
                    ) {
                        Row(
                            modifier = Modifier.padding(Dimensions.spacingLg),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Surface(
                                shape = MaterialTheme.shapes.extraLarge,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(Dimensions.profileImageSize),
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = user.name.take(1).uppercase(),
                                        style = MaterialTheme.typography.headlineLarge,
                                        color = MaterialTheme.colorScheme.onPrimary,
                                    )
                                }
                            }
                            Spacer(Modifier.width(Dimensions.spacingMd))
                            Column {
                                Text(user.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                                Text(user.email, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f))
                                if (user.role.name == "ADMIN") {
                                    Spacer(Modifier.height(Dimensions.spacingXs))
                                    AssistChip(onClick = {}, label = { Text("Admin") }, leadingIcon = { Icon(Icons.Default.AdminPanelSettings, contentDescription = null, modifier = Modifier.size(Dimensions.iconSizeSm)) })
                                }
                            }
                        }
                    }

                    // Stats
                    Card(modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.large) {
                        Column(modifier = Modifier.padding(Dimensions.spacingMd)) {
                            Text("Lichaamsinformatie", style = MaterialTheme.typography.titleMedium)
                            Spacer(Modifier.height(Dimensions.spacingMd))
                            if (user.weight != null) ProfileInfoRow(Icons.Default.FitnessCenter, "Gewicht", "${user.weight} kg")
                            if (user.height != null) ProfileInfoRow(Icons.Default.Height, "Lengte", "${user.height} cm")
                            if (user.age != null) ProfileInfoRow(Icons.Default.Cake, "Leeftijd", "${user.age} jaar")
                            if (user.gender != null) ProfileInfoRow(Icons.Default.Person, "Geslacht", user.gender.name.lowercase().replaceFirstChar { it.uppercase() })
                        }
                    }

                    // Goals
                    Card(modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.large) {
                        Column(modifier = Modifier.padding(Dimensions.spacingMd)) {
                            Text("Sportdoelen", style = MaterialTheme.typography.titleMedium)
                            Spacer(Modifier.height(Dimensions.spacingMd))
                            ProfileInfoRow(Icons.Default.Flag, "Doel", user.goal.displayName)
                            ProfileInfoRow(Icons.Default.DirectionsRun, "Activiteitsniveau", user.activityLevel.displayName)
                        }
                    }

                    // Nutrition goals
                    val goals = uiState.nutritionGoals
                    if (goals != null) {
                        Card(modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.large) {
                            Column(modifier = Modifier.padding(Dimensions.spacingMd)) {
                                Text("Voedingsdoelen", style = MaterialTheme.typography.titleMedium)
                                Spacer(Modifier.height(Dimensions.spacingMd))
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                                    NutritionGoalItem("BMR", "${goals.bmr} kcal")
                                    VerticalDivider(modifier = Modifier.height(40.dp))
                                    NutritionGoalItem("TDEE", "${goals.tdee} kcal", CalorieColor)
                                    VerticalDivider(modifier = Modifier.height(40.dp))
                                    NutritionGoalItem("Eiwit doel", "${goals.proteinGoal}g", ProteinColor)
                                }
                                Spacer(Modifier.height(Dimensions.spacingMd))
                                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer), shape = MaterialTheme.shapes.medium) {
                                    Text(
                                        text = "Caloriedoel: ${goals.goalCalories} kcal/dag",
                                        modifier = Modifier.fillMaxWidth().padding(Dimensions.spacingMd),
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileInfoRow(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = Dimensions.spacingXs),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(Dimensions.iconSizeMd), tint = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.width(Dimensions.spacingMd))
        Text(label, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun NutritionGoalItem(label: String, value: String, color: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = color)
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
