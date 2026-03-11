package com.example.randomnumbergenerator

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val etMin = findViewById<EditText>(R.id.etMin)
        val etMax = findViewById<EditText>(R.id.etMax)
        val btnGenerate = findViewById<Button>(R.id.btnGenerate)
        val tvResult = findViewById<TextView>(R.id.tvResult)

        btnGenerate.setOnClickListener {

            val minText = etMin.text.toString()
            val maxText = etMax.text.toString()

            if (minText.isEmpty() || maxText.isEmpty()) {
                Toast.makeText(this, "Enter both Min and Max values", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val min = minText.toInt()
            val max = maxText.toInt()

            if (min > max) {
                Toast.makeText(this, "Min value should be less than Max", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val randomNumber = Random.nextInt(min, max + 1)
            tvResult.text = "Random Number: $randomNumber"
        }
    }
}