package com.sportsnutrition.presentation.screens.restaurant

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sportsnutrition.presentation.components.AppSearchBar
import com.sportsnutrition.presentation.components.ErrorView
import com.sportsnutrition.presentation.components.LoadingIndicator
import com.sportsnutrition.presentation.components.RestaurantCard
import com.sportsnutrition.presentation.theme.Dimensions
import com.sportsnutrition.presentation.viewmodels.RestaurantViewModel

private val CUISINE_FILTERS = listOf("Italiaans", "Aziatisch", "Mexicaans", "Grieks", "Japans", "Nederlands", "Amerikaans")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantListScreen(
    onRestaurantClick: (String) -> Unit,
    onBack: () -> Unit,
    viewModel: RestaurantViewModel = hiltViewModel(),
) {
    val uiState by viewModel.listState.collectAsStateWithLifecycle()
    val query by viewModel.searchQuery.collectAsStateWithLifecycle()
    var selectedCuisine by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Restaurants") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Terug")
                    }
                },
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding),
        ) {
            Column(modifier = Modifier.padding(Dimensions.spacingMd)) {
                AppSearchBar(
                    query = query,
                    onQueryChange = { viewModel.updateSearch(it) },
                    placeholder = "Zoek op naam of stad...",
                )
                Spacer(Modifier.height(Dimensions.spacingSm))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(Dimensions.spacingXs)) {
                    items(CUISINE_FILTERS) { cuisine ->
                        FilterChip(
                            selected = selectedCuisine == cuisine,
                            onClick = {
                                selectedCuisine = if (selectedCuisine == cuisine) null else cuisine
                                viewModel.updateCuisineFilter(selectedCuisine)
                            },
                            label = { Text(cuisine) },
                        )
                    }
                }
            }

            when {
                uiState.isLoading && uiState.restaurants.isEmpty() -> LoadingIndicator(message = "Restaurants laden...")
                uiState.error != null && uiState.restaurants.isEmpty() -> ErrorView(
                    message = uiState.error!!,
                    onRetry = { viewModel.updateSearch(query) },
                )
                uiState.restaurants.isEmpty() -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = androidx.compose.ui.Alignment.Center,
                ) { Text("Geen restaurants gevonden", style = MaterialTheme.typography.bodyLarge) }
                else -> LazyColumn(
                    contentPadding = PaddingValues(Dimensions.spacingMd),
                    verticalArrangement = Arrangement.spacedBy(Dimensions.spacingMd),
                ) {
                    items(uiState.restaurants, key = { it.id }) { restaurant ->
                        RestaurantCard(
                            restaurant = restaurant,
                            onClick = { onRestaurantClick(restaurant.id) },
                        )
                    }
                    if (uiState.isLoading) {
                        item { LinearProgressIndicator(modifier = Modifier.fillMaxWidth()) }
                    }
                }
            }
        }
    }
}
