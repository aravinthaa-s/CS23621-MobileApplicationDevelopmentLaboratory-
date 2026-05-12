package com.example.campusfoodapp.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.campusfoodapp.R
import com.example.campusfoodapp.adapters.MenuAdapter
import com.example.campusfoodapp.models.MenuItem
import com.example.campusfoodapp.utils.CartManager
import com.google.firebase.firestore.FirebaseFirestore

class MenuActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private val menuList = mutableListOf<MenuItem>()
    private lateinit var adapter: MenuAdapter
    private lateinit var shopId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        db = FirebaseFirestore.getInstance()
        shopId = intent.getStringExtra("shopId") ?: ""
        val shopName = intent.getStringExtra("shopName") ?: "Menu"

        findViewById<TextView>(R.id.tvMenuHeader).text = shopName

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewMenu)
        val btnCart = findViewById<Button>(R.id.btnCart)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MenuAdapter(menuList) { item ->
            CartManager.addItem(item)
            Toast.makeText(this, "${item.name} added to cart", Toast.LENGTH_SHORT).show()
        }
        recyclerView.adapter = adapter

        btnCart.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }

        loadMenu()
    }

    private fun loadMenu() {
        if (shopId.isEmpty()) return
        
        db.collection("shops").document(shopId).collection("menu")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Toast.makeText(this, "Error loading menu", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }
                
                menuList.clear()
                value?.documents?.forEach { doc ->
                    val item = doc.toObject(MenuItem::class.java)
                    if (item != null) {
                        item.id = doc.id
                        menuList.add(item)
                    }
                }
                adapter.notifyDataSetChanged()
            }
    }
}
