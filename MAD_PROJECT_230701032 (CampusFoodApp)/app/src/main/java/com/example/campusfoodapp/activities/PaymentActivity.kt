package com.example.campusfoodapp.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.campusfoodapp.R
import com.example.campusfoodapp.models.MenuItem
import com.example.campusfoodapp.models.Order
import com.example.campusfoodapp.models.User
import com.example.campusfoodapp.utils.CartManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import java.util.Locale

class PaymentActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    
    private lateinit var tvOrderId: TextView
    private lateinit var tvItems: TextView
    private lateinit var tvDelivery: TextView
    private lateinit var tvTotal: TextView
    private lateinit var btnPay: Button
    private lateinit var progressBar: ProgressBar

    private var totalAmount: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        tvOrderId = findViewById(R.id.tvPaymentOrderId)
        tvItems = findViewById(R.id.tvPaymentItems)
        tvDelivery = findViewById(R.id.tvPaymentDelivery)
        tvTotal = findViewById(R.id.tvPaymentTotal)
        btnPay = findViewById(R.id.btnPayNow)
        progressBar = findViewById(R.id.paymentProgressBar)

        val tempOrderId = "TEMP_" + System.currentTimeMillis()
        tvOrderId.text = "Order ID: #$tempOrderId"

        // Display items
        val itemsText = CartManager.cartItems.joinToString("\n") { "${it.name} x ${it.quantity}" }
        tvItems.text = itemsText

        // Calculate total
        totalAmount = 0.0
        for (item in CartManager.cartItems) {
            val price = item.price.replace(Regex("[^\\d.]"), "").toDoubleOrNull() ?: 0.0
            totalAmount += price * item.quantity
        }
        tvTotal.text = String.format(Locale.getDefault(), "₹%.2f", totalAmount)

        loadUserDetails()

        btnPay.setOnClickListener {
            simulatePayment()
        }
    }

    private fun loadUserDetails() {
        val uid = auth.currentUser?.uid ?: return
        db.collection("users").document(uid).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val user = document.toObject(User::class.java)
                    user?.let {
                        tvDelivery.text = "Name: ${it.name}\nRoll No: ${it.rollNumber}\nPhone: ${it.phoneNumber}"
                    }
                }
            }
    }

    private fun simulatePayment() {
        btnPay.visibility = View.GONE
        progressBar.visibility = View.VISIBLE

        // Simulate network delay for payment
        Handler(Looper.getMainLooper()).postDelayed({
            placeOrderInFirestore()
        }, 2000)
    }

    private fun placeOrderInFirestore() {
        val userId = auth.currentUser?.uid ?: ""
        val orderId = db.collection("orders").document().id
        val timestamp = System.currentTimeMillis()

        val order = Order(
            orderId = orderId,
            userId = userId,
            items = CartManager.cartItems.toList(),
            totalAmount = totalAmount,
            timestamp = timestamp,
            status = "Paid" // Update status to Paid
        )

        db.collection("orders")
            .document(orderId)
            .set(order)
            .addOnSuccessListener {
                Toast.makeText(this, "Payment Successful!", Toast.LENGTH_SHORT).show()
                
                val intent = Intent(this, OrderConfirmationActivity::class.java)
                intent.putExtra("orderId", orderId)
                intent.putExtra("timestamp", timestamp)
                intent.putExtra("totalAmount", totalAmount)
                intent.putExtra("itemsJson", Gson().toJson(CartManager.cartItems))
                
                CartManager.cartItems.clear()
                
                startActivity(intent)
                finish()
            }
            .addOnFailureListener {
                btnPay.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
                Toast.makeText(this, "Failed to place order after payment", Toast.LENGTH_SHORT).show()
            }
    }
}
