package com.example.inicioactivity

import android.os.Bundle
import android.widget.Toast
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.example.inicioactivity.database.AppDatabase
import com.example.inicioactivity.database.Usuario
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class Register : AppCompatActivity() {

    // 1. Declarar una variable para la base de datos.
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Esto le dice a la app que puede dibujar debajo de las barras del sistema.
        WindowCompat.setDecorFitsSystemWindows(window, false)
        // Obtenemos el controlador de las barras del sistema.
        val insetsController = WindowCompat.getInsetsController(window, window.decorView)
        // Hacemos que la barra de estado sea transparente para poder ponerle nuestro color.
        window.statusBarColor = Color.TRANSPARENT
        // deben ser de color claro para que se vean bien sobre nuestro fondo oscuro.
        insetsController.isAppearanceLightStatusBars = false

        setContentView(R.layout.activity_register)

        // 2. Inicializar la base de datos.
        db = AppDatabase.getDatabase(this)

        val buttonRegister = findViewById<MaterialButton>(R.id.buttonRegister)
        val editTextUsername = findViewById<TextInputEditText>(R.id.editTextUsername)
        val editTextEmail = findViewById<TextInputEditText>(R.id.editTextEmail)
        val editTextPassword = findViewById<TextInputEditText>(R.id.editTextPassword)
        val loginLink = findViewById<TextView>(R.id.textViewLoginLink)

        loginLink.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            // Ejecutamos el intent para abrir la pantalla de Login
            startActivity(intent)
            // Cerramos la pantalla de Registro para que el usuario no pueda
            // volver a ella presionando el botón "atrás" desde el Login.
            finish()
        }

        buttonRegister.setOnClickListener {
            val username = editTextUsername.text.toString().trim()
            val email = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString().trim()

            if (username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                // 3. Usar una coroutine para no bloquear la pantalla.
                lifecycleScope.launch {
                    // ¡OJO! En una app real, la contraseña debe ser "hasheada" (cifrada) antes de guardarse.
                    // Por ahora, la guardamos así como ejemplo.
                    val nuevoUsuario = Usuario(
                        nombreUsuario = username,
                        correo = email,
                        hashContrasena = password
                    )

                    // 4. Llamar al DAO para insertar el usuario.
                    db.usuarioDao().insertarUsuario(nuevoUsuario)

                    // 5. Mostrar un mensaje de éxito en la pantalla.
                    runOnUiThread {
                        Toast.makeText(this@Register, "¡Usuario registrado con éxito!", Toast.LENGTH_SHORT).show()
                        finish() // Opcional: Cierra la pantalla de registro y vuelve a la anterior.
                    }
                }
            } else {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
