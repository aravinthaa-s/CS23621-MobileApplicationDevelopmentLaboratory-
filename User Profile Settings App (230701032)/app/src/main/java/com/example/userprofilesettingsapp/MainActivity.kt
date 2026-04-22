package com.example.userprofilesettingsapp

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etAge: EditText
    private lateinit var etBio: EditText
    private lateinit var radioGroup: RadioGroup
    private lateinit var btnSave: Button

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Init views
        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etAge = findViewById(R.id.etAge)
        etBio = findViewById(R.id.etBio)
        radioGroup = findViewById(R.id.radioGroupColor)
        btnSave = findViewById(R.id.btnSave)

        // SharedPreferences
        sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE)

        // Load saved data automatically
        loadData()

        // Save button click
        btnSave.setOnClickListener {
            saveData()
            Toast.makeText(this, "Profile Saved", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveData() {
        val editor = sharedPreferences.edit()

        editor.putString("name", etName.text.toString())
        editor.putString("email", etEmail.text.toString())
        editor.putString("age", etAge.text.toString())
        editor.putString("bio", etBio.text.toString())

        // Save selected radio button
        val selectedColorId = radioGroup.checkedRadioButtonId
        val selectedColor = findViewById<RadioButton>(selectedColorId)?.text.toString()
        editor.putString("color", selectedColor)

        editor.apply()
    }

    private fun loadData() {
        etName.setText(sharedPreferences.getString("name", ""))
        etEmail.setText(sharedPreferences.getString("email", ""))
        etAge.setText(sharedPreferences.getString("age", ""))
        etBio.setText(sharedPreferences.getString("bio", ""))

        val color = sharedPreferences.getString("color", "")

        when (color) {
            "Red" -> radioGroup.check(R.id.rbRed)
            "Blue" -> radioGroup.check(R.id.rbBlue)
            "Green" -> radioGroup.check(R.id.rbGreen)
        }
    }
}