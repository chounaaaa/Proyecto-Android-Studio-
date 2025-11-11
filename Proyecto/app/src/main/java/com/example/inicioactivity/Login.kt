package com.example.inicioactivity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.inicioactivity.databinding.ActivityLoginBinding

class Login : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonLogin.setOnClickListener {
            val email = binding.Email.text.toString()
            val password = binding.Password.text.toString()

            val preferencias: SharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
            val emailRegistrado = preferencias.getString("email", "")
            val passwordRegistrada = preferencias.getString("password", "")

            if (email.isNotEmpty() && password.isNotEmpty()) {
                if (email == emailRegistrado && password == passwordRegistrada) {
                    Toast.makeText(this, "¡Bienvenido!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MenuPrincipal::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Email o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}