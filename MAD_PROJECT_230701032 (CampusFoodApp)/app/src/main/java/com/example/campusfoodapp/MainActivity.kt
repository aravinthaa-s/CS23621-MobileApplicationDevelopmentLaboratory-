package com.example.campusfoodapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.FirebaseApp

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase
        FirebaseApp.initializeApp(this)

        // Test message
        Toast.makeText(this, "Firebase Connected!", Toast.LENGTH_SHORT).show()

        setContentView(R.layout.activity_main)
    }
}