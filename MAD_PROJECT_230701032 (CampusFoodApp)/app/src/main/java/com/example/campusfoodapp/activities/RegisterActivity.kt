package com.example.campusfoodapp.activities

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.campusfoodapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.campusfoodapp.models.User

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val etName = findViewById<EditText>(R.id.etName)
        val etRollNumber = findViewById<EditText>(R.id.etRollNumber)
        val etPhoneNumber = findViewById<EditText>(R.id.etPhoneNumber)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val registerBtn = findViewById<Button>(R.id.btnRegister)
        val roleGroup = findViewById<RadioGroup>(R.id.roleGroup)

        registerBtn.setOnClickListener {
            val name = etName.text.toString()
            val rollNumber = etRollNumber.text.toString()
            val phoneNumber = etPhoneNumber.text.toString()
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            if (name.isEmpty() || rollNumber.isEmpty() || phoneNumber.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show()
            } else {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val uid = auth.currentUser!!.uid
                            val role = when (roleGroup.checkedRadioButtonId) {
                                R.id.rbShopkeeper -> "shopkeeper"
                                else -> "student"
                            }

                            val user = User(uid, name, email, rollNumber, phoneNumber, role)

                            db.collection("users")
                                .document(uid)
                                .set(user)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Registered Successfully", Toast.LENGTH_SHORT).show()
                                    finish()
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(this, "Firestore Error: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                        } else {
                            Toast.makeText(this, task.exception?.message.toString(), Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }
    }
}
