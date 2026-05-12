package com.example.campusfoodapp.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.campusfoodapp.R
import com.example.campusfoodapp.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    
    private lateinit var etName: EditText
    private lateinit var etRoll: EditText
    private lateinit var etPhone: EditText
    private lateinit var tvEmail: TextView
    private lateinit var btnUpdate: Button
    private lateinit var btnLogout: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        etName = findViewById(R.id.etProfileName)
        etRoll = findViewById(R.id.etProfileRoll)
        etPhone = findViewById(R.id.etProfilePhone)
        tvEmail = findViewById(R.id.tvProfileEmail)
        btnUpdate = findViewById(R.id.btnUpdateProfile)
        btnLogout = findViewById(R.id.btnLogout)

        loadUserProfile()

        btnUpdate.setOnClickListener {
            updateProfile()
        }

        btnLogout.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun loadUserProfile() {
        val uid = auth.currentUser?.uid ?: return
        db.collection("users").document(uid).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val user = document.toObject(User::class.java)
                    user?.let {
                        etName.setText(it.name)
                        etRoll.setText(it.rollNumber)
                        etPhone.setText(it.phoneNumber)
                        tvEmail.text = it.email
                    }
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load profile", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateProfile() {
        val uid = auth.currentUser?.uid ?: return
        val name = etName.text.toString()
        val roll = etRoll.text.toString()
        val phone = etPhone.text.toString()

        if (name.isEmpty() || roll.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val updates = mapOf(
            "name" to name,
            "rollNumber" to roll,
            "phoneNumber" to phone
        )

        db.collection("users").document(uid).update(updates)
            .addOnSuccessListener {
                Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Update Failed", Toast.LENGTH_SHORT).show()
            }
    }
}
