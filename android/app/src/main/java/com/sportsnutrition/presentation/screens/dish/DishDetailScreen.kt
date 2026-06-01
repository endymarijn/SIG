package com.sportsnutrition.presentation.screens.dish

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.sportsnutrition.presentation.components.ErrorView
import com.sportsnutrition.presentation.components.LoadingIndicator
import com.sportsnutrition.presentation.components.NutrientCard
import com.sportsnutrition.presentation.components.NutritionRow
import com.sportsnutrition.presentation.theme.*
import com.sportsnutrition.presentation.viewmodels.DishViewModel
import com.sportsnutrition.utils.formatNutrient

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DishDetailScreen(
    dishId: String,
    onBack: () -> Unit,
    viewModel: DishViewModel = hiltViewModel(),
) {
    val uiState by viewModel.detailState.collectAsStateWithLifecycle()

    LaunchedEffect(dishId) { viewModel.loadDishDetail(dishId) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.dish?.name ?: "Gerecht") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Terug")
                    }
                },
            )
        }
    ) { padding ->
        when {
            uiState.isLoading -> LoadingIndicator()
            uiState.error != null -> ErrorView(message = uiState.error!!, onRetry = { viewModel.loadDishDetail(dishId) })
            else -> {
                val dish = uiState.dish ?: return@Scaffold
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .verticalScroll(rememberScrollState()),
                ) {
                    if (dish.imageUrl != null) {
                        AsyncImage(
                            model = dish.imageUrl,
                            contentDescription = dish.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(240.dp)
                                .clip(MaterialTheme.shapes.extraLarge),
                        )
                    }

                    Column(modifier = Modifier.padding(Dimensions.spacingMd)) {
                        Text(dish.name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)

                        if (dish.restaurantName != null) {
                            Text(dish.restaurantName, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
                        }

                        if (!dish.description.isNullOrBlank()) {
                            Spacer(Modifier.height(Dimensions.spacingMd))
                            Text(dish.description, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }

                        // Nutrition tags
                        if (dish.tags.isNotEmpty()) {
                            Spacer(Modifier.height(Dimensions.spacingMd))
                            Row(horizontalArrangement = Arrangement.spacedBy(Dimensions.spacingXs), modifier = Modifier.fillMaxWidth()) {
                                dish.tags.forEach { tag ->
                                    AssistChip(
                                        onClick = {},
                                        label = { Text(tag.displayName) },
                                        leadingIcon = { Text(tag.emoji) },
                                        colors = AssistChipDefaults.assistChipColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(Dimensions.spacingLg))
                        Text("Voedingswaarden", style = MaterialTheme.typography.titleLarge)
                        Text("Per portie (${dish.servingSize}g)", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)

                        Spacer(Modifier.height(Dimensions.spacingMd))
                        NutritionRow(dish.calories, dish.protein, dish.carbs, dish.fat)

                        Spacer(Modifier.height(Dimensions.spacingMd))

                        // Detailed nutrients
                        Card(modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.large) {
                            Column(modifier = Modifier.padding(Dimensions.spacingMd)) {
                                NutrientRow("Vezels", dish.fiber, "g", FiberColor)
                                HorizontalDivider(modifier = Modifier.padding(vertical = Dimensions.spacingXs))
                                NutrientRow("Suikers", dish.sugar, "g", CarbsColor)
                                HorizontalDivider(modifier = Modifier.padding(vertical = Dimensions.spacingXs))
                                NutrientRow("Natrium", dish.sodium, "mg", MaterialTheme.colorScheme.outline)
                                if (dish.price != null) {
                                    HorizontalDivider(modifier = Modifier.padding(vertical = Dimensions.spacingXs))
                                    NutrientRow("Prijs", dish.price, "€", MaterialTheme.colorScheme.primary)
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(Dimensions.spacingXl))
                }
            }
        }
    }
}

@Composable
private fun NutrientRow(label: String, value: Double, unit: String, color: androidx.compose.ui.graphics.Color) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = Dimensions.spacingXs),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
        Text("${value.formatNutrient()} $unit", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium, color = color)
    }
}
