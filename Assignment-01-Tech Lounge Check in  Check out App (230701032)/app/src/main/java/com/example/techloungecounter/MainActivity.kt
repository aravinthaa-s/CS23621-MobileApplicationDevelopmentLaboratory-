package com.example.techloungecounter

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var count = 0   // Counter variable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Link UI elements
        val tvCount = findViewById<TextView>(R.id.tvCount)
        val btnCheckIn = findViewById<Button>(R.id.btnCheckIn)
        val btnCheckOut = findViewById<Button>(R.id.btnCheckOut)

        // Restore count after rotation (if any)
        count = savedInstanceState?.getInt("COUNT_KEY") ?: 0
        tvCount.text = count.toString()

        // Check In Button
        btnCheckIn.setOnClickListener {
            count++
            tvCount.text = count.toString()
        }

        // Check Out Button
        btnCheckOut.setOnClickListener {
            if (count > 0) {
                count--
                tvCount.text = count.toString()
            }
        }
    }

    // Save counter value during configuration changes
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("COUNT_KEY", count)
    }
}