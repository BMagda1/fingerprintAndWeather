package com.example.fingerprint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.fingerprint.LoginActivity
import com.example.fingerprint.R
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        val emailEditText = findViewById<EditText>(R.id.usernameEdit)
        val passwordEditText = findViewById<EditText>(R.id.passwordEdit)
        val registerButton = findViewById<Button>(R.id.registerButton)

        registerButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                registerUser(email, password)
            } else {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Înregistrarea cu succes, poți afișa un mesaj sau efectua alte acțiuni.
                    Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
                    // Deschidem activitatea de login
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)

                    // Închidem activitatea curentă
                    finish()
                } else {
                    // Înregistrarea a eșuat, afișează un mesaj de eroare sau efectuează alte acțiuni.
                    Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}

