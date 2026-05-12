package com.example.campusfoodapp.activities

import android.os.Bundle
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.campusfoodapp.R
import com.example.campusfoodapp.adapters.ShopAdapter
import com.example.campusfoodapp.models.Shop
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

class ShopActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private val shopList = mutableListOf<Shop>()
    private lateinit var adapter: ShopAdapter
    private var userRole: String = "student"

    private val qrScannerLauncher = registerForActivityResult(ScanContract()) { result ->
        if (result.contents != null) {
            val scannedData = result.contents
            // Try to extract Order ID from the plain text receipt format: "ORDER ID : #ID"
            val idPattern = "ORDER ID : #([^\\n]+)".toRegex()
            val match = idPattern.find(scannedData)
            val orderId = match?.groupValues?.get(1)?.trim()

            if (orderId != null) {
                val intent = Intent(this, ReceiptActivity::class.java)
                intent.putExtra("orderId", orderId)
                startActivity(intent)
            } else {
                // If it's not a structured receipt, just show the text in a dialog
                AlertDialog.Builder(this)
                    .setTitle("Scanned Receipt")
                    .setMessage(scannedData)
                    .setPositiveButton("OK", null)
                    .show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop)

        db = FirebaseFirestore.getInstance()
        userRole = intent.getStringExtra("userRole") ?: "student"
        
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewShops)
        val fabAddShop = findViewById<FloatingActionButton>(R.id.fabAddShop)
        val fabScanQR = findViewById<FloatingActionButton>(R.id.fabScanQR)
        val ivProfile = findViewById<ImageView>(R.id.ivProfile)

        if (userRole == "shopkeeper") {
            fabAddShop.visibility = View.VISIBLE
            fabScanQR.visibility = View.VISIBLE
        }

        ivProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        fabScanQR.setOnClickListener {
            val options = ScanOptions()
            options.setPrompt("Scan Order QR Code")
            options.setBeepEnabled(true)
            options.setOrientationLocked(true)
            options.setCaptureActivity(CaptureActivityPortrait::class.java)
            qrScannerLauncher.launch(options)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ShopAdapter(
            shopList,
            isAdmin = (userRole == "shopkeeper"),
            onEdit = { shop -> showAddEditShopDialog(shop) },
            onDelete = { shop -> deleteShop(shop) },
            onClick = { shop ->
                val targetActivity = if (userRole == "shopkeeper") {
                    ShopkeeperMenuActivity::class.java
                } else {
                    MenuActivity::class.java
                }
                
                val intent = Intent(this, targetActivity)
                intent.putExtra("shopName", shop.name)
                intent.putExtra("shopId", shop.id)
                startActivity(intent)
            }
        )
        recyclerView.adapter = adapter

        fabAddShop.setOnClickListener {
            showAddEditShopDialog(null)
        }

        checkAndAddDefaultShops()
        loadShops()
    }

    private fun checkAndAddDefaultShops() {
        db.collection("shops").get().addOnSuccessListener { result ->
            if (result.isEmpty) {
                val defaultShops = listOf("Dominos", "BlackBuck", "RecCafe", "HutCafe", "RecMart", "CoffeeDay")
                for (shopName in defaultShops) {
                    addShop(shopName)
                }
            }
        }
    }

    private fun loadShops() {
        db.collection("shops")
            .addSnapshotListener { value, error ->
                if (error != null) return@addSnapshotListener
                shopList.clear()
                value?.documents?.forEach { doc ->
                    val shop = doc.toObject(Shop::class.java)
                    if (shop != null) {
                        shopList.add(shop.copy(id = doc.id))
                    }
                }
                adapter.notifyDataSetChanged()
            }
    }

    private fun showAddEditShopDialog(shop: Shop?) {
        val builder = AlertDialog.Builder(this)
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_add_menu_item, null)
        val etName = view.findViewById<EditText>(R.id.etDialogItemName)
        val etPrice = view.findViewById<EditText>(R.id.etDialogItemPrice)
        val etDesc = view.findViewById<EditText>(R.id.etDialogItemDescription)
        
        etPrice.visibility = View.GONE
        etDesc.visibility = View.GONE

        etName.hint = "Shop Name"
        if (shop != null) {
            etName.setText(shop.name)
        }

        builder.setView(view)
            .setTitle(if (shop == null) "Add New Shop" else "Update Shop")
            .setPositiveButton(if (shop == null) "Add" else "Update") { _, _ ->
                val name = etName.text.toString()
                if (name.isNotEmpty()) {
                    if (shop == null) addShop(name) else updateShop(shop.id, name)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun addShop(name: String) {
        val newShop = hashMapOf("name" to name)
        db.collection("shops").add(newShop)
    }

    private fun updateShop(id: String, name: String) {
        db.collection("shops").document(id).update("name", name)
            .addOnSuccessListener {
                Toast.makeText(this, "Shop updated", Toast.LENGTH_SHORT).show()
            }
    }

    private fun deleteShop(shop: Shop) {
        db.collection("shops").document(shop.id).delete()
            .addOnSuccessListener {
                Toast.makeText(this, "Shop deleted", Toast.LENGTH_SHORT).show()
            }
    }
}
