package com.sportsnutrition.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.AsyncImage
import com.sportsnutrition.domain.models.Dish
import com.sportsnutrition.presentation.theme.CalorieColor
import com.sportsnutrition.presentation.theme.Dimensions
import com.sportsnutrition.presentation.theme.ProteinColor
import com.sportsnutrition.utils.formatNutrient

@Composable
fun DishCard(
    dish: Dish,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth().clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = Dimensions.cardElevation),
        shape = MaterialTheme.shapes.large,
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            if (dish.imageUrl != null) {
                AsyncImage(
                    model = dish.imageUrl,
                    contentDescription = dish.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(120.dp)
                        .fillMaxHeight()
                        .clip(MaterialTheme.shapes.large),
                )
            } else {
                Box(
                    modifier = Modifier.size(120.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text("🍽️", style = MaterialTheme.typography.headlineMedium)
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(Dimensions.spacingMd),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Column {
                    Text(
                        text = dish.name,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                    if (!dish.description.isNullOrBlank()) {
                        Spacer(Modifier.height(Dimensions.spacingXs))
                        Text(
                            text = dish.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }

                Spacer(Modifier.height(Dimensions.spacingSm))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(Dimensions.spacingMd),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "${dish.calories.toInt()} kcal",
                        style = MaterialTheme.typography.labelMedium,
                        color = CalorieColor,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = "${dish.protein.formatNutrient()}g eiwit",
                        style = MaterialTheme.typography.labelMedium,
                        color = ProteinColor,
                        fontWeight = FontWeight.Medium,
                    )
                }

                if (dish.tags.isNotEmpty()) {
                    Spacer(Modifier.height(Dimensions.spacingXs))
                    Row(horizontalArrangement = Arrangement.spacedBy(Dimensions.spacingXs)) {
                        dish.tags.take(2).forEach { tag ->
                            AssistChip(
                                onClick = {},
                                label = { Text(tag.displayName, style = MaterialTheme.typography.labelSmall) },
                                leadingIcon = { Text(tag.emoji) },
                            )
                        }
                    }
                }
            }
        }
    }
}
