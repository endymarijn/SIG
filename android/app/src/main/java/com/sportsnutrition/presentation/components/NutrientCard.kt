package com.sportsnutrition.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sportsnutrition.presentation.theme.CalorieColor
import com.sportsnutrition.presentation.theme.CarbsColor
import com.sportsnutrition.presentation.theme.Dimensions
import com.sportsnutrition.presentation.theme.FatColor
import com.sportsnutrition.presentation.theme.ProteinColor
import com.sportsnutrition.utils.formatNutrient

@Composable
fun NutrientCard(
    label: String,
    value: Double,
    unit: String,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.12f)),
        shape = MaterialTheme.shapes.medium,
    ) {
        Column(
            modifier = Modifier.padding(Dimensions.spacingMd),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(color, shape = MaterialTheme.shapes.extraSmall)
            )
            Spacer(Modifier.height(Dimensions.spacingXs))
            Text(
                text = "${value.formatNutrient()}$unit",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = color,
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
fun NutritionRow(calories: Double, protein: Double, carbs: Double, fat: Double) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Dimensions.spacingSm),
    ) {
        NutrientCard("Calorieën", calories, " kcal", CalorieColor, Modifier.weight(1f))
        NutrientCard("Eiwit", protein, "g", ProteinColor, Modifier.weight(1f))
        NutrientCard("Koolh.", carbs, "g", CarbsColor, Modifier.weight(1f))
        NutrientCard("Vet", fat, "g", FatColor, Modifier.weight(1f))
    }
}
