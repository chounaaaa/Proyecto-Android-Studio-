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
    // 1. Declaramos la variable para la base de datos
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 2. Inicializamos la base de datos usando applicationContext para seguridad
        db = AppDatabase.getDatabase(applicationContext)

        // Usa el ID que definimos en el XML (textViewRegisterLink).
        binding.textViewRegisterLink.setOnClickListener {
            // Creamos una intención para ir desde aquí (Login) a la pantalla de Register.
            val intent = Intent(this, Register::class.java)
            // Ejecutamos la intención.
            startActivity(intent)
        }

        // Lógica del botón de Login
        binding.buttonLogin.setOnClickListener {
            // 3. Leemos los datos del formulario (corrigiendo el uso de mayúsculas/minúsculas)
            val email = binding.Email.text.toString().trim()
            val password = binding.Password.text.toString().trim()

            // Verificamos que los campos no estén vacíos antes de consultar la DB
            if (email.isNotEmpty() && password.isNotEmpty()) {
                // 4. Lanzamos una corutina para no bloquear la UI
                lifecycleScope.launch {
                    // Buscamos en la base de datos por el email introducido
                    val usuario = db.usuarioDao().buscarPorEmail(email)

                    // 5. Volvemos al hilo principal para mostrar los resultados
                    runOnUiThread {
                        if (usuario != null && usuario.hashContrasena == password) {
                            // ¡Éxito! El usuario existe y la contraseña coincide
                            Toast.makeText(this@Login, "¡Bienvenido ${usuario.nombreUsuario}!", Toast.LENGTH_SHORT).show()

                            // Navegamos al menú principal
                            val intent = Intent(this@Login, MenuPrincipal::class.java)
                            startActivity(intent)
                            finish() // Cerramos la pantalla de Login para que no pueda volver atrás
                        } else {
                            // Error: El usuario no existe o la contraseña es incorrecta
                            Toast.makeText(this@Login, "Email o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                // Error: Los campos están vacíos
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
