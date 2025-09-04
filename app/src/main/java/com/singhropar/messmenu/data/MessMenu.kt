package com.singhropar.messmenu.data

data class MessMenuItem(
    val hostel: String,
    val menu: Map<String, Meals>
)

data class Meals(
    val breakfast: List<String>,
    val lunch: List<String>,
    val dinner: List<String>
)
