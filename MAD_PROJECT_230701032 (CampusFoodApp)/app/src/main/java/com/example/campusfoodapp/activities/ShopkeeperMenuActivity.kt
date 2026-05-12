package com.example.campusfoodapp.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.campusfoodapp.R
import com.example.campusfoodapp.adapters.ShopkeeperMenuAdapter
import com.example.campusfoodapp.models.MenuItem
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class ShopkeeperMenuActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var adapter: ShopkeeperMenuAdapter
    private val itemList = mutableListOf<MenuItem>()
    private lateinit var shopId: String
    
    private var imageUri: Uri? = null
    private var currentDialogImageView: ImageView? = null

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            imageUri = result.data?.data
            currentDialogImageView?.setImageURI(imageUri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopkeeper_menu)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        
        shopId = intent.getStringExtra("shopId") ?: ""
        val shopName = intent.getStringExtra("shopName") ?: "Shop"
        
        findViewById<TextView>(R.id.tvMenuTitle).apply {
            text = "$shopName Menu"
        }

        val rv = findViewById<RecyclerView>(R.id.rvShopkeeperMenu)
        rv.layoutManager = LinearLayoutManager(this)
        adapter = ShopkeeperMenuAdapter(itemList, 
            onEdit = { item -> showAddEditDialog(item) },
            onDelete = { item -> deleteItem(item) }
        )
        rv.adapter = adapter

        findViewById<FloatingActionButton>(R.id.fabAddMenuItem).setOnClickListener {
            showAddEditDialog(null)
        }

        loadMenu()
    }

    private fun loadMenu() {
        if (shopId.isEmpty()) return
        db.collection("shops").document(shopId).collection("menu")
            .addSnapshotListener { value, error ->
                if (error != null) return@addSnapshotListener
                itemList.clear()
                value?.documents?.forEach { doc ->
                    val item = doc.toObject(MenuItem::class.java)
                    if (item != null) {
                        item.id = doc.id
                        itemList.add(item)
                    }
                }
                adapter.notifyDataSetChanged()
            }
    }

    private fun showAddEditDialog(item: MenuItem?) {
        val builder = AlertDialog.Builder(this)
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_add_menu_item, null)
        val etName = view.findViewById<EditText>(R.id.etDialogItemName)
        val etPrice = view.findViewById<EditText>(R.id.etDialogItemPrice)
        val etDesc = view.findViewById<EditText>(R.id.etDialogItemDescription)
        val ivItem = view.findViewById<ImageView>(R.id.ivDialogItemImage)
        val btnSelect = view.findViewById<Button>(R.id.btnSelectImage)

        currentDialogImageView = ivItem
        imageUri = null

        if (item != null) {
            etName.setText(item.name)
            etPrice.setText(item.price)
            etDesc.setText(item.description)
            if (item.imageUrl.isNotEmpty()) {
                Glide.with(this).load(item.imageUrl).into(ivItem)
            }
        }

        btnSelect.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            pickImageLauncher.launch(intent)
        }

        builder.setView(view)
            .setTitle(if (item == null) "Add Menu Item" else "Update Menu Item")
            .setPositiveButton(if (item == null) "Add" else "Update") { _, _ ->
                val name = etName.text.toString()
                val price = etPrice.text.toString()
                val desc = etDesc.text.toString()
                
                if (name.isNotEmpty() && price.isNotEmpty()) {
                    if (imageUri != null) {
                        uploadImageAndSaveItem(item?.id, name, price, desc)
                    } else {
                        if (item == null) {
                            addItem(name, price, desc, "")
                        } else {
                            updateItem(item.id, name, price, desc, item.imageUrl)
                        }
                    }
                } else {
                    Toast.makeText(this, "Name and Price are required", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun uploadImageAndSaveItem(id: String?, name: String, price: String, desc: String) {
        val fileName = UUID.randomUUID().toString()
        val ref = storage.reference.child("menu_images/$fileName")

        ref.putFile(imageUri!!)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener { url ->
                    if (id == null) {
                        addItem(name, price, desc, url.toString())
                    } else {
                        updateItem(id, name, price, desc, url.toString())
                    }
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show()
            }
    }

    private fun addItem(name: String, price: String, description: String, imageUrl: String) {
        val newItem = MenuItem("", name, price, description, imageUrl)
        db.collection("shops").document(shopId).collection("menu")
            .add(newItem)
            .addOnSuccessListener {
                Toast.makeText(this, "Item added", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateItem(id: String, name: String, price: String, description: String, imageUrl: String) {
        val updatedItem = mutableMapOf(
            "name" to name,
            "price" to price,
            "description" to description,
            "imageUrl" to imageUrl
        )
        db.collection("shops").document(shopId).collection("menu").document(id)
            .update(updatedItem as Map<String, Any>)
            .addOnSuccessListener {
                Toast.makeText(this, "Item updated", Toast.LENGTH_SHORT).show()
            }
    }

    private fun deleteItem(item: MenuItem) {
        AlertDialog.Builder(this)
            .setTitle("Delete Item")
            .setMessage("Are you sure you want to delete ${item.name}?")
            .setPositiveButton("Delete") { _, _ ->
                db.collection("shops").document(shopId).collection("menu").document(item.id)
                    .delete()
                    .addOnSuccessListener {
                        Toast.makeText(this, "Item deleted", Toast.LENGTH_SHORT).show()
                    }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
