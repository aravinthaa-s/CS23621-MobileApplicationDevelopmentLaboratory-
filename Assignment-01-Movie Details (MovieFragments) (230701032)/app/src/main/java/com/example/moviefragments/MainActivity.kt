package com.example.moviefragments

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {

    lateinit var btnBasic: Button
    lateinit var btnAdditional: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnBasic = findViewById<Button>(R.id.btnBasic)
        val btnAdditional = findViewById<Button>(R.id.btnAdditional)

        btnBasic.setOnClickListener {
            loadFragment(MovieBasicFragment())
        }

        btnAdditional.setOnClickListener {
            loadFragment(MovieAdditionalFragment())
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}