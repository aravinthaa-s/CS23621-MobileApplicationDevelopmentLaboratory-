package com.example.campusfoodapp.models

data class Order(
    val orderId: String = "",
    val userId: String = "",
    val items: List<MenuItem> = listOf(),
    val totalAmount: Double = 0.0,
    val timestamp: Long = 0L,
    val status: String = "Preparing"
)
