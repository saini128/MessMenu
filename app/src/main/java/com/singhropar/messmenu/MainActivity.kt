package com.singhropar.messmenu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.singhropar.messmenu.ui.theme.MessMenuTheme
import com.singhropar.messmenu.viewmodel.MessViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MessMenuTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MessMenuScreen()
                }
            }
        }
    }
}

@Composable
fun MessMenuScreen(viewModel: MessViewModel = viewModel()) {
    val menu by viewModel.menu.collectAsState()

    if (menu.isEmpty()) {
        Text("Loading...", modifier = Modifier.padding(16.dp))
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            items(menu) { hostelMenu ->
                Text(hostelMenu.hostel, style = MaterialTheme.typography.titleLarge)

                hostelMenu.menu.forEach { (day, meals) ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(day.uppercase(), style = MaterialTheme.typography.titleMedium)

                    Text("Breakfast: ${meals.breakfast.joinToString()}")
                    Text("Lunch: ${meals.lunch.joinToString()}")
                    Text("Dinner: ${meals.dinner.joinToString()}")

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}
