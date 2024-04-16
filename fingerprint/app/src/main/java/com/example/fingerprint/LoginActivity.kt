package com.example.fingerprint

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        val emailEditText = findViewById<EditText>(R.id.userNameTextField)
        val passwordEditText = findViewById<EditText>(R.id.passwordTextField)
        val loginButton = findViewById<Button>(R.id.submitButton)
        val fingerprintButton: Button = findViewById(R.id.fingerprintButton)

        // Setarea unui listener pentru click pe butonul "Use Fingerprint"
        fingerprintButton.setOnClickListener {
            // Deschiderea activității de autentificare cu amprentă
            val intent = Intent(this, FingerprintActivity::class.java)
            startActivity(intent)
        }
        val signUpButton: Button = findViewById(R.id.SignUpButton)

        signUpButton.setOnClickListener {
            // Deschide activitatea de înregistrare (SignUpActivity)
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginUser(email, password)
            } else {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Autentificare cu succes, poți redirecționa utilizatorul către pagina principală sau alt ecran dorit.
                    val intent = Intent(this, WeatherActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // Autentificarea a eșuat, afișează un mesaj de eroare sau efectuează alte acțiuni.
                    Toast.makeText(this, "Authentication failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}