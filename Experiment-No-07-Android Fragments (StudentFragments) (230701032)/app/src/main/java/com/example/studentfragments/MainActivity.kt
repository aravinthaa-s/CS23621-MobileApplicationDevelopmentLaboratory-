package com.example.studentfragments

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnBasic = findViewById<Button>(R.id.btnBasic)
        val btnMark = findViewById<Button>(R.id.btnMark)

        btnBasic.setOnClickListener {
            loadFragment(StudentBasicFragment())
        }

        btnMark.setOnClickListener {
            loadFragment(StudentMarkFragment())
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}