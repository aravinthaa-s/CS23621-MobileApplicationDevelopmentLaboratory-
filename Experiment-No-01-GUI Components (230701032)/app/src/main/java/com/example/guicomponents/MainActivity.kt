package com.example.guicomponents

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView = findViewById<TextView>(R.id.tvText)
        val btnFont = findViewById<Button>(R.id.btnFont)
        val btnColor = findViewById<Button>(R.id.btnColor)

        // Button to change font
        btnFont.setOnClickListener {
            textView.setTypeface(null, Typeface.BOLD_ITALIC)
            Toast.makeText(this, "Font Changed", Toast.LENGTH_SHORT).show()
        }

        // Button to change color
        btnColor.setOnClickListener {
            textView.setTextColor(Color.BLUE)
            Toast.makeText(this, "Color Changed", Toast.LENGTH_SHORT).show()
        }
    }
}