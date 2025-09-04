package com.singhropar.messmenu.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.singhropar.messmenu.data.Meals

@Composable
fun MenuCard(day: String, meals: Meals, expandable: Boolean = false) {
    var expanded by remember { mutableStateOf(!expandable) }

    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(day, style = MaterialTheme.typography.titleMedium)
                if (expandable) {
                    TextButton(onClick = { expanded = !expanded }) {
                        Text(if (expanded) "Hide" else "View")
                    }
                }
            }

            AnimatedVisibility(
                visible = expanded,
                enter = androidx.compose.animation.fadeIn(animationSpec = tween(300)),
                exit = androidx.compose.animation.fadeOut(animationSpec = tween(300))
            ) {
                Column {
                    Text("🍳 Breakfast: ${meals.breakfast.joinToString()}")
                    Text("🍲 Lunch: ${meals.lunch.joinToString()}")
                    Text("🍛 Dinner: ${meals.dinner.joinToString()}")
                }
            }
        }
    }
}
