package com.example.recdigitalcafeloginvalidation


import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val email = findViewById<EditText>(R.id.etEmail)
        val password = findViewById<EditText>(R.id.etPassword)
        val validateBtn = findViewById<Button>(R.id.btnValidate)

        validateBtn.setOnClickListener {

            val emailText = email.text.toString().trim()
            val passwordText = password.text.toString()


            if (emailText.isEmpty() || passwordText.isEmpty()) {
                Toast.makeText(this, "Fields should not be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            val collegeEmailPattern = Regex("^[a-zA-Z0-9._%+-]+@rajalakshmi\\.edu\\.in$")
            if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()
                || !collegeEmailPattern.matches(emailText)) {
                Toast.makeText(this, "Enter a valid college email ID", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            val passwordPattern = Regex(
                "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#\$%^&+=!]).{12,}$"
            )

            if (!passwordPattern.matches(passwordText)) {
                Toast.makeText(
                    this,
                    "Password must be 12 characters with uppercase, number & special character",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }


            Toast.makeText(this, "Login Validation Successful", Toast.LENGTH_SHORT).show()
        }
    }
}