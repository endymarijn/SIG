package com.sportsnutrition.presentation.screens.restaurant

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.sportsnutrition.presentation.components.DishCard
import com.sportsnutrition.presentation.components.ErrorView
import com.sportsnutrition.presentation.components.LoadingIndicator
import com.sportsnutrition.presentation.theme.Dimensions
import com.sportsnutrition.presentation.viewmodels.DishViewModel
import com.sportsnutrition.presentation.viewmodels.RestaurantViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantDetailScreen(
    restaurantId: String,
    onDishClick: (String) -> Unit,
    onBack: () -> Unit,
    restaurantViewModel: RestaurantViewModel = hiltViewModel(),
    dishViewModel: DishViewModel = hiltViewModel(),
) {
    val restaurantState by restaurantViewModel.detailState.collectAsStateWithLifecycle()
    val dishState by dishViewModel.listState.collectAsStateWithLifecycle()

    LaunchedEffect(restaurantId) {
        restaurantViewModel.loadRestaurantDetail(restaurantId)
        dishViewModel.loadDishesForRestaurant(restaurantId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(restaurantState.restaurant?.name ?: "Restaurant") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Terug")
                    }
                },
            )
        }
    ) { padding ->
        when {
            restaurantState.isLoading -> LoadingIndicator()
            restaurantState.error != null -> ErrorView(
                message = restaurantState.error!!,
                onRetry = { restaurantViewModel.loadRestaurantDetail(restaurantId) },
            )
            else -> {
                val restaurant = restaurantState.restaurant ?: return@Scaffold
                LazyColumn(modifier = Modifier.fillMaxSize().padding(padding)) {
                    item {
                        if (restaurant.imageUrl != null) {
                            AsyncImage(
                                model = restaurant.imageUrl,
                                contentDescription = restaurant.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(220.dp)
                                    .clip(MaterialTheme.shapes.extraLarge),
                            )
                        }
                        Column(modifier = Modifier.padding(Dimensions.spacingMd)) {
                            Text(restaurant.name, style = MaterialTheme.typography.headlineSmall)
                            if (!restaurant.description.isNullOrBlank()) {
                                Spacer(Modifier.height(Dimensions.spacingXs))
                                Text(
                                    restaurant.description,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                            Spacer(Modifier.height(Dimensions.spacingMd))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.LocationOn, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(Dimensions.iconSizeMd))
                                Spacer(Modifier.width(Dimensions.spacingXs))
                                Text("${restaurant.address}, ${restaurant.city}", style = MaterialTheme.typography.bodyMedium)
                            }
                            if (!restaurant.phoneNumber.isNullOrBlank()) {
                                Spacer(Modifier.height(Dimensions.spacingXs))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(Dimensions.iconSizeMd))
                                    Spacer(Modifier.width(Dimensions.spacingXs))
                                    Text(restaurant.phoneNumber, style = MaterialTheme.typography.bodyMedium)
                                }
                            }

                            Spacer(Modifier.height(Dimensions.spacingLg))
                            HorizontalDivider()
                            Spacer(Modifier.height(Dimensions.spacingMd))
                            Text("Menu", style = MaterialTheme.typography.titleLarge)
                        }
                    }

                    when {
                        dishState.isLoading -> item { LoadingIndicator() }
                        dishState.error != null -> item { ErrorView(message = dishState.error!!) }
                        else -> items(dishState.dishes, key = { it.id }) { dish ->
                            DishCard(
                                dish = dish,
                                onClick = { onDishClick(dish.id) },
                                modifier = Modifier.padding(horizontal = Dimensions.spacingMd, vertical = Dimensions.spacingXs),
                            )
                        }
                    }

                    item { Spacer(Modifier.height(Dimensions.spacingXl)) }
                }
            }
        }
    }
}
