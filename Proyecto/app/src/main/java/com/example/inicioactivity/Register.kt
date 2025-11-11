package com.example.inicioactivity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.example.inicioactivity.database.AppDatabase
import com.example.inicioactivity.database.Usuario
import com.example.inicioactivity.databinding.ActivityRegisterBinding
import kotlinx.coroutines.launch

class Register : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. TU CÓDIGO para la barra de estado (¡correcto!).
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val insetsController = WindowCompat.getInsetsController(window, window.decorView)
        window.statusBarColor = Color.TRANSPARENT
        insetsController.isAppearanceLightStatusBars = false

        // 2. CÓDIGO DE TU AMIGO para inicializar ViewBinding (¡correcto!).
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 3. TU CÓDIGO para inicializar la base de datos (¡correcto!).
        db = AppDatabase.getDatabase(this)

        // 4. Enlace para ir al Login (¡correcto!).
        binding.textViewLoginLink.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }

        // 5. Lógica del botón de registro.
        binding.buttonRegister.setOnClickListener {

            // --- ¡AQUÍ ESTÁ LA CORRECCIÓN CLAVE! ---
            // Usamos los nombres correctos que ViewBinding genera desde tus IDs de XML.
            // Asegúrate de que los IDs en tu activity_register.xml sean correctos.
            val username = binding.Username.text.toString().trim()
            val email = binding.Email.text.toString().trim()
            val password = binding.Password.text.toString().trim()
            // --- FIN DE LA CORRECCIÓN ---

            val camposNoEstanVacios = username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()
            val emailEsValido = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

            // Ahora este IF funcionará como esperas.
            if (camposNoEstanVacios && emailEsValido) {
                lifecycleScope.launch {
                    val nuevoUsuario = Usuario(
                        nombreUsuario = username,
                        correo = email,
                        hashContrasena = password
                    )

                    // TU forma correcta y segura de acceder a la DB.
                    AppDatabase.getDatabase(applicationContext).usuarioDao().insertarUsuario(nuevoUsuario)

                    // TU forma correcta de navegar DESPUÉS de guardar.
                    runOnUiThread {
                        Toast.makeText(this@Register, "¡Usuario registrado con éxito!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@Register, Login::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            } else {
                // Lógica de error de TU AMIGO (¡correcta!).
                if (!camposNoEstanVacios) {
                    Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "El formato del correo electrónico no es válido", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
