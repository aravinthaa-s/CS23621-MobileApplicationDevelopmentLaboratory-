package com.example.radiobutton

import android.graphics.Color
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.RadioGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val layout = findViewById<LinearLayout>(R.id.main)
        val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)

        radioGroup.setOnCheckedChangeListener { _, checkedId ->

            when (checkedId) {

                R.id.radioRed -> layout.setBackgroundColor(Color.RED)

                R.id.radioGreen -> layout.setBackgroundColor(Color.GREEN)

                R.id.radioBlue -> layout.setBackgroundColor(Color.BLUE)
            }
        }
    }
}