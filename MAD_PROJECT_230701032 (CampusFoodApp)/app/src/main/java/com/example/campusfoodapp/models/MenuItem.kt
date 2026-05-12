package com.example.campusfoodapp.models

data class MenuItem(
    var id: String = "",
    val name: String = "",
    val price: String = "",
    val description: String = "",
    val imageUrl: String = "",
    var quantity: Int = 0
)
