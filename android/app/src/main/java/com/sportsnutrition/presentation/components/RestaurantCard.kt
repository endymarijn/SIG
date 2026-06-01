package com.sportsnutrition.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.AsyncImage
import com.sportsnutrition.domain.models.Restaurant
import com.sportsnutrition.presentation.theme.Dimensions

@Composable
fun RestaurantCard(
    restaurant: Restaurant,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = Dimensions.cardElevation),
        shape = MaterialTheme.shapes.large,
    ) {
        Column {
            if (restaurant.imageUrl != null) {
                AsyncImage(
                    model = restaurant.imageUrl,
                    contentDescription = restaurant.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dimensions.restaurantCardImageHeight)
                        .clip(MaterialTheme.shapes.large),
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dimensions.restaurantCardImageHeight),
                    contentAlignment = Alignment.Center,
                ) {
                    Text("🍽️", style = MaterialTheme.typography.displaySmall)
                }
            }

            Column(modifier = Modifier.padding(Dimensions.spacingMd)) {
                Text(
                    text = restaurant.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                if (!restaurant.description.isNullOrBlank()) {
                    Spacer(Modifier.height(Dimensions.spacingXs))
                    Text(
                        text = restaurant.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                }

                Spacer(Modifier.height(Dimensions.spacingXs))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        modifier = Modifier.size(Dimensions.iconSizeSm),
                        tint = MaterialTheme.colorScheme.primary,
                    )
                    Spacer(Modifier.width(Dimensions.spacingXs))
                    Text(
                        text = "${restaurant.city}${if (restaurant.postalCode != null) " · ${restaurant.postalCode}" else ""}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }

                if (restaurant.cuisineType.isNotEmpty()) {
                    Spacer(Modifier.height(Dimensions.spacingXs))
                    Row(horizontalArrangement = Arrangement.spacedBy(Dimensions.spacingXs)) {
                        restaurant.cuisineType.take(3).forEach { cuisine ->
                            SuggestionChip(
                                onClick = {},
                                label = { Text(cuisine, style = MaterialTheme.typography.labelSmall) },
                            )
                        }
                    }
                }
            }
        }
    }
}
