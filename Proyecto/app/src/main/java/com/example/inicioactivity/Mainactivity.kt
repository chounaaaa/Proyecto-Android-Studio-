package com.example.inicioactivity

import android.content.Intent
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.inicioactivity.databinding.ActivityMainBinding

// Esta es la clase para la pantalla de bienvenida.
// Su nombre coincide con el archivo XML `activity_main.xml`.
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Efecto de desenfoque para el fondo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val blurEffect = RenderEffect.createBlurEffect(15f, 15f, Shader.TileMode.CLAMP)
            binding.backgroundImage.setRenderEffect(blurEffect)
        }

        // --- CÓDIGO CLAVE PARA EL SALTO ---
        // Al presionar el botón con ID "buttonPrueba"...
        binding.buttonPrueba.setOnClickListener {
            // 1. Se crea una "intención" de ir desde esta pantalla (this) hacia la clase `MenuPrincipal`.
            val intent = Intent(this, MenuPrincipal::class.java)

            // 2. Se ejecuta la intención, abriendo la pantalla del menú.
            startActivity(intent)
        }

        // Listeners para los otros botones (Login y Register)
        binding.button2.setOnClickListener {
            // val intent = Intent(this, Login::class.java)
            // startActivity(intent)
        }

        binding.buttonRegister.setOnClickListener {
            // val intent = Intent(this, Register::class.java)
            // startActivity(intent)
        }
    }
}
