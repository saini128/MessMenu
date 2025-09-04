package com.singhropar.messmenu.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.singhropar.messmenu.viewmodel.MessViewModel
import com.singhropar.messmenu.ui.components.HostelSelector
import com.singhropar.messmenu.ui.components.MenuCard
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessMenuScreen(viewModel: MessViewModel = viewModel()) {
    val menu by viewModel.menu.collectAsState()
    var selectedHostel by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mess Menu", style = MaterialTheme.typography.titleLarge) }
            )
        }
    ) { innerPadding ->
        if (menu.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    HostelSelector(
                        hostels = menu.map { it.hostel },
                        selectedHostel = selectedHostel,
                        onHostelSelected = { selectedHostel = it }
                    )
                }

                selectedHostel?.let { hostel ->
                    val today = LocalDate.now().dayOfWeek
                        .getDisplayName(TextStyle.FULL, Locale.getDefault())
                    val hostelMenu = menu.find { it.hostel.equals(hostel, ignoreCase = true) }
                    val todayMeals = hostelMenu?.menu?.entries
                        ?.find { it.key.equals(today, ignoreCase = true) }?.value

                    if (todayMeals != null) {
                        item {
                            Text(
                                text = "Today's Menu - $today",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(Modifier.height(8.dp))
                            MenuCard(day = today, meals = todayMeals)
                        }

                        item {
                            Text("Other Days", style = MaterialTheme.typography.titleMedium)
                        }

                        items(hostelMenu.menu.entries.filter { !it.key.equals(today, ignoreCase = true) }) { (day, meals) ->
                            MenuCard(day = day, meals = meals, expandable = true)
                        }
                    } else {
                        item {
                            Text(
                                "Menu not available for $today",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    }
}
