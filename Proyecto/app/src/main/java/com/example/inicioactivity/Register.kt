package com.example.inicioactivity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.inicioactivity.databinding.ActivityRegisterBinding

class Register : AppCompatActivity() {

    // Declara una variable para poder usar las vistas de tu XML (botones, textos, etc.)
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Prepara la pantalla para que se muestre, usando tu archivo activity_register.xml
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // --- Configuración del botón de Registro ---
// Esto le dice a la app qué hacer cuando el usuario presione el botón "Registrar"
        binding.buttonRegister.setOnClickListener {
            // Paso 1: Leer lo que el usuario escribió en los campos de texto.
            val username = binding.Username.text.toString()
            val email = binding.Email.text.toString()
            val password = binding.Password.text.toString()

            // Paso 2: Validar los datos (¡tu propuesta!).
            // Primero, verificamos que los campos no estén vacíos.
            val camposNoEstanVacios = username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()

            // Segundo, verificamos que el email tenga un formato válido (opcional pero muy recomendado).
            val emailEsValido = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

            // Usamos el IF que sugeriste: Si los campos no están vacíos Y el email es válido...
            if (camposNoEstanVacios && emailEsValido) {
                // ... entonces todo está correcto y procedemos.

                // Acción 1: Guardar los datos del usuario.
                val preferencias: SharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
                val editor = preferencias.edit()

                editor.putString("username", username)
                editor.putString("email", email)
                editor.putString("password", password) // En una app real, ¡cifrar esto!
                editor.apply()

                // Acción 2: Informar al usuario que el registro fue exitoso.
                Toast.makeText(this, "¡Registro completado!", Toast.LENGTH_SHORT).show()

                // Acción 3: Crear el Intent y navegar a la pantalla de Login.
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
                finish() // Cerramos la pantalla de registro.

            } else {
                // Si la validación falla (algo está vacío o el email es incorrecto)...
                // ... mostramos un mensaje de error general.
                // Podríamos incluso ser más específicos.
                if (!camposNoEstanVacios) {
                    Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
                } else { // Si los campos están llenos, el problema debe ser el email.
                    Toast.makeText(this, "El formato del correo electrónico no es válido", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
}
