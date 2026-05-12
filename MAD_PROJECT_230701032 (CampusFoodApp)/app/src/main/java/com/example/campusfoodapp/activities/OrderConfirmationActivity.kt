package com.example.campusfoodapp.activities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.campusfoodapp.R
import com.example.campusfoodapp.models.MenuItem
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import java.text.SimpleDateFormat
import java.util.*

class OrderConfirmationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_confirmation)

        val orderId = intent.getStringExtra("orderId") ?: ""
        val timestamp = intent.getLongExtra("timestamp", System.currentTimeMillis())
        val totalAmount = intent.getDoubleExtra("totalAmount", 0.0)
        val itemsJson = intent.getStringExtra("itemsJson") ?: "[]"
        
        val items = Gson().fromJson(itemsJson, Array<MenuItem>::class.java).toList()

        findViewById<TextView>(R.id.tvOrderNumber).text = "Order ID: #$orderId"
        
        val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
        val formattedTime = sdf.format(Date(timestamp))
        findViewById<TextView>(R.id.tvOrderTime).text = "Time: $formattedTime"
        
        val itemsText = items.joinToString("\n") { "${it.name} x ${it.quantity}" }
        findViewById<TextView>(R.id.tvOrderItems).text = itemsText
        
        findViewById<TextView>(R.id.tvTotalAmount).text = "₹${String.format(Locale.getDefault(), "%.2f", totalAmount)}"

        // Payment status is always Successful here since we arrive after PaymentActivity
        findViewById<TextView>(R.id.tvPaymentStatus).text = "Successful"

        // Build the plain text receipt for the QR code
        // We use Rs. instead of ₹ to ensure standard scanners display it correctly
        val qrContent = StringBuilder().apply {
            append("CAMPUS FOOD APP\n")
            append("---------------------------\n")
            append("ORDER ID : #$orderId\n")
            append("DATE     : $formattedTime\n")
            append("---------------------------\n")
            append("ITEMS:\n")
            append(itemsText).append("\n")
            append("---------------------------\n")
            append(String.format(Locale.US, "TOTAL    : Rs. %.2f\n", totalAmount))
            append("STATUS   : PAID\n")
            append("---------------------------\n")
            append("Thank you for your order!")
        }.toString()

        val ivQRCode = findViewById<ImageView>(R.id.ivQRCode)
        val qrBitmap = generateQRCode(qrContent)
        if (qrBitmap != null) {
            ivQRCode.setImageBitmap(qrBitmap)
        }

        findViewById<Button>(R.id.btnBackToHome).setOnClickListener {
            val intent = Intent(this, ShopActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    private fun generateQRCode(content: String): Bitmap? {
        val writer = QRCodeWriter()
        try {
            val hints = mutableMapOf<EncodeHintType, Any>()
            hints[EncodeHintType.CHARACTER_SET] = "UTF-8"
            hints[EncodeHintType.MARGIN] = 2
            hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.M // Medium error correction for better scannability
            
            val bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, 512, 512, hints)
            val width = bitMatrix.width
            val height = bitMatrix.height
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bitmap.setPixel(x, y, if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE)
                }
            }
            return bitmap
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}
