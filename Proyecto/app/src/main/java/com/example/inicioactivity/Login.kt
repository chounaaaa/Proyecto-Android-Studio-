package com.example.inicioactivity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.inicioactivity.database.AppDatabase
import com.example.inicioactivity.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch

class Login : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    // --- CAMBIO 1: Ya no necesitamos una variable 'db' a nivel de clase ---
    // private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // --- CAMBIO 2: Eliminamos la inicialización local de la base de datos ---
        // db = AppDatabase.getDatabase(applicationContext)

        binding.textViewRegisterLink.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }

        binding.buttonLogin.setOnClickListener {
            val email = binding.Email.text.toString().trim()
            val password = binding.Password.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                lifecycleScope.launch {
                    // --- CAMBIO 3: Usamos la instancia centralizada de MyApplication ---
                    // Obtenemos la instancia ÚNICA de la base de datos.
                    val db = (application as MyApplication).database
                    val usuario = db.usuarioDao().buscarPorEmail(email)

                    runOnUiThread {
                        if (usuario != null && usuario.hashContrasena == password) {
                            Toast.makeText(this@Login, "¡Bienvenido ${usuario.nombreUsuario}!", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@Login, MenuPrincipal::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this@Login, "Email o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
