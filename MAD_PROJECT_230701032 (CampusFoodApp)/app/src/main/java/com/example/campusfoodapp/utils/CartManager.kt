package com.example.campusfoodapp.utils

import com.example.campusfoodapp.models.MenuItem

object CartManager {
    val cartItems = mutableListOf<MenuItem>()

    fun addItem(item: MenuItem) {
        val existingItem = cartItems.find { it.name == item.name }

        if (existingItem != null) {
            existingItem.quantity++
        } else {
            // Create a copy to avoid reference issues and ensure quantity is set to 1
            val newItem = item.copy(quantity = 1)
            cartItems.add(newItem)
        }
    }
}
