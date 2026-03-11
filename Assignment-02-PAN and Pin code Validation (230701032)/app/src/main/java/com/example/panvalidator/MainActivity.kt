package com.example.panvalidator
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etPan = findViewById<EditText>(R.id.etPan)
        val etPincode = findViewById<EditText>(R.id.etPincode)
        val btnValidate = findViewById<Button>(R.id.btnValidate)

        btnValidate.setOnClickListener {

            val pan = etPan.text.toString().trim()
            val pincode = etPincode.text.toString().trim()


            if (pan.isEmpty() || pincode.isEmpty()) {
                Toast.makeText(this, "Fields should not be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            val panPattern = Regex("^[A-Za-z0-9]{10}$")
            if (!panPattern.matches(pan)) {
                Toast.makeText(
                    this,
                    "PAN Card Number must be alphanumeric and 10 characters",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }


            val pincodePattern = Regex("^\\d{6}$")
            if (!pincodePattern.matches(pincode)) {
                Toast.makeText(
                    this,
                    "Pincode must be exactly 6 digits",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }


            Toast.makeText(this, "Validation Successful", Toast.LENGTH_SHORT).show()
        }
    }
}