package com.example.inicioactivity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.inicioactivity.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch

class Login : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textViewRegisterLink.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }

        binding.buttonLogin.setOnClickListener {
            val email = binding.Email.text.toString().trim()
            val password = binding.Password.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                lifecycleScope.launch {
                    val db = (application as MyApplication).database
                    val usuario = db.usuarioDao().buscarPorEmail(email)

                    runOnUiThread {
                        if (usuario != null && usuario.hashContrasena == password) {
                            Toast.makeText(this@Login, "¡Bienvenido ${usuario.nombreUsuario}!", Toast.LENGTH_SHORT).show()

                            // --- INICIO DE LA CORRECCIÓN ---

                            // 1. Obtenemos acceso a SharedPreferences.
                            val sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

                            // 2. Abrimos el editor para guardar datos.
                            with(sharedPref.edit()) {
                                // 3. Guardamos el ID del usuario.
                                putInt("USER_ID", usuario.id_usuario)

                                // 4. ¡NUEVO! Guardamos también el nombre de usuario.
                                putString("USER_NAME", usuario.nombreUsuario)

                                // 5. Confirmamos y aplicamos los cambios.
                                apply()
                            }

                            // --- FIN DE LA CORRECCIÓN ---

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
