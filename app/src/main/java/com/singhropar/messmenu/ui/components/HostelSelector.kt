package com.singhropar.messmenu.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HostelSelector(
    hostels: List<String>,
    selectedHostel: String?,
    onHostelSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedHostel ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text("Select Hostel") },
            modifier = Modifier.menuAnchor(),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            }
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            hostels.forEach { hostel ->
                DropdownMenuItem(
                    text = { Text(hostel) },
                    onClick = {
                        onHostelSelected(hostel)
                        expanded = false
                    }
                )
            }
        }
    }
}
