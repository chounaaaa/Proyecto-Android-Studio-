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
    // Mantenemos la declaración, pero seremos más cuidadosos al usarla.
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        val insetsController = WindowCompat.getInsetsController(window, window.decorView)
        window.statusBarColor = Color.TRANSPARENT
        insetsController.isAppearanceLightStatusBars = false

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializamos db aquí como antes.
        db = AppDatabase.getDatabase(this)

        binding.textViewLoginLink.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }

        binding.buttonRegister.setOnClickListener {
            val username = binding.Username.text.toString().trim()
            val email = binding.Email.text.toString().trim()
            val password = binding.Password.text.toString().trim()

            val emailEsValido = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
            val camposNoEstanVacios = username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()

            if (camposNoEstanVacios && emailEsValido) {
                // --- CAMBIO CLAVE: Usamos 'this@Register' como contexto para la corutina ---
                // y para la base de datos, garantizando que el contexto está vivo.
                lifecycleScope.launch {
                    val nuevoUsuario = Usuario(
                        nombreUsuario = username,
                        correo = email,
                        hashContrasena = password
                    )

                    // Accedemos a la base de datos dentro de la corutina para más seguridad
                    AppDatabase.getDatabase(applicationContext).usuarioDao().insertarUsuario(nuevoUsuario)

                    // Volvemos al hilo principal para la UI
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
