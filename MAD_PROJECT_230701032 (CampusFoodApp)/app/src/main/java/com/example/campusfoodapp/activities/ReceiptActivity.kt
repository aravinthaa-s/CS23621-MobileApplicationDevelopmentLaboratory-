package com.example.campusfoodapp.activities

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.campusfoodapp.R
import com.example.campusfoodapp.models.Order
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class ReceiptActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receipt)

        db = FirebaseFirestore.getInstance()
        
        // Handle both Deep Link and direct Intent
        val orderId = intent.data?.getQueryParameter("orderId") ?: intent.getStringExtra("orderId")

        if (orderId != null) {
            fetchOrderDetails(orderId)
        } else {
            Toast.makeText(this, "Invalid Order ID", Toast.LENGTH_SHORT).show()
            finish()
        }

        findViewById<Button>(R.id.btnReceiptClose).setOnClickListener {
            finish()
        }
    }

    private fun fetchOrderDetails(orderId: String) {
        db.collection("orders").document(orderId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val order = document.toObject(Order::class.java)
                    if (order != null) {
                        displayOrder(order)
                    }
                } else {
                    Toast.makeText(this, "Order not found", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error fetching order", Toast.LENGTH_SHORT).show()
                finish()
            }
    }

    private fun displayOrder(order: Order) {
        findViewById<TextView>(R.id.tvReceiptOrderId).text = "Order ID: #${order.orderId}"
        
        val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
        findViewById<TextView>(R.id.tvReceiptDate).text = "Date: ${sdf.format(Date(order.timestamp))}"
        
        val itemsText = order.items.joinToString("\n") { "${it.name} x ${it.quantity}" }
        findViewById<TextView>(R.id.tvReceiptItemsList).text = itemsText
        
        findViewById<TextView>(R.id.tvReceiptTotalAmount).text = "₹${String.format(Locale.getDefault(), "%.2f", order.totalAmount)}"
        findViewById<TextView>(R.id.tvReceiptStatus).text = order.status
        
        // Change status color based on status
        val statusTv = findViewById<TextView>(R.id.tvReceiptStatus)
        if (order.status == "Paid" || order.status == "Delivered") {
            statusTv.setBackgroundResource(android.R.color.holo_green_light)
        } else {
            statusTv.setBackgroundResource(android.R.color.holo_orange_light)
        }
    }
}
