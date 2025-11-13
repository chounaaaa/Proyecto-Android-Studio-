package com.example.inicioactivity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.inicioactivity.database.AppDatabase
import com.example.inicioactivity.database.Usuario
import com.example.inicioactivity.databinding.ActivityRegisterBinding
import kotlinx.coroutines.launch

class Register : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    // --- CAMBIO 1: Ya no necesitamos una variable 'db' a nivel de clase ---
    // private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // --- CAMBIO 2: Eliminamos la inicialización local de la base de datos ---
        // db = AppDatabase.getDatabase(this)

        binding.textViewLoginLink.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }

        binding.buttonRegister.setOnClickListener {
            val username = binding.Username.text.toString().trim()
            val email = binding.Email.text.toString().trim()
            val password = binding.Password.text.toString().trim()

            val camposNoEstanVacios = username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()
            val emailEsValido = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

            if (camposNoEstanVacios && emailEsValido) {
                lifecycleScope.launch {
                    val nuevoUsuario = Usuario(
                        nombreUsuario = username,
                        correo = email,
                        hashContrasena = password
                    )

                    // --- CAMBIO 3: Usamos la instancia centralizada de MyApplication ---
                    // Obtenemos la instancia única de la base de datos desde nuestra clase Application.
                    val db = (application as MyApplication).database
                    db.usuarioDao().insertarUsuario(nuevoUsuario)

                    runOnUiThread {
                        Toast.makeText(this@Register, "¡Usuario registrado con éxito!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@Register, Login::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            } else {
                if (!camposNoEstanVacios) {
                    Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "El formato del correo electrónico no es válido", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
