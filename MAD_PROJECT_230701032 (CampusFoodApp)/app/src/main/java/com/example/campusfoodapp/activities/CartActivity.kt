package com.example.campusfoodapp.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.campusfoodapp.R
import com.example.campusfoodapp.adapters.CartAdapter
import com.example.campusfoodapp.utils.CartManager
import com.google.gson.Gson

class CartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewCart)
        val btnPlaceOrder = findViewById<Button>(R.id.btnPlaceOrder)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = CartAdapter(CartManager.cartItems)

        btnPlaceOrder.setOnClickListener {
            if (CartManager.cartItems.isEmpty()) {
                Toast.makeText(this, "Cart is empty", Toast.LENGTH_SHORT).show()
            } else {
                // Navigate to Payment Activity instead of placing order directly
                val intent = Intent(this, PaymentActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
