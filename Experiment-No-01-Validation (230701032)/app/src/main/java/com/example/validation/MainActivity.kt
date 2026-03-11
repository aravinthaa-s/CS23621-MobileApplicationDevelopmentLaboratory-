package com.example.validation

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etId = findViewById<EditText>(R.id.etId)
        val btnValidate = findViewById<Button>(R.id.btnValidate)

        btnValidate.setOnClickListener {

            val username = etUsername.text.toString().trim()
            val id = etId.text.toString().trim()


            if (username.isEmpty() || id.isEmpty()) {
                Toast.makeText(this, "Fields should not be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val namePattern = Regex("^[A-Za-z]+$")
            if (!namePattern.matches(username)) {
                Toast.makeText(this, "Username should contain only alphabets", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            val idPattern = Regex("^\\d{4}$")
            if (!idPattern.matches(id)) {
                Toast.makeText(this, "ID should be exactly 4 digits", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            Toast.makeText(this, "Validation Successful", Toast.LENGTH_SHORT).show()
        }
    }
}