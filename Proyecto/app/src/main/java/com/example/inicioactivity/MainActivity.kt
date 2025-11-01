package com.example.inicioactivity

import android.content.Intent // <-- IMPORTACIÓN AÑADIDA
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.os.Bundle
import android.widget.Button // <-- IMPORTACIÓN AÑADIDA
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton // <-- IMPORTACIÓN AÑADIDA

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Imagen borrosa de fondo

        // Encontramos el ImageView por su ID.
        val backgroundImageView = findViewById<ImageView>(R.id.background_image)

        // RenderEffect solo funciona en Android 12 (API 31) y superior.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // 3. Esta es la línea corregida que debes poner
            val blurEffect = RenderEffect.createBlurEffect(20f, 20f, Shader.TileMode.CLAMP)

            // Aplicamos el efecto a nuestro ImageView.
            backgroundImageView.setRenderEffect(blurEffect)
        }

        // --- CÓDIGO AÑADIDO PARA EL BOTÓN REGISTRAR ---
        // 1. Encontrar el botón "Registrar" por su ID del XML.
        val registerButton = findViewById<MaterialButton>(R.id.buttonRegister)

        // 2. Asignarle una acción para cuando se haga clic.
        registerButton.setOnClickListener {
            // 3. Crear un Intent que apunte a la actividad Register.
            val intent = Intent(this, Register::class.java)

            // 4. Iniciar la nueva actividad.
            startActivity(intent)
        }
        // --- FIN DEL CÓDIGO AÑADIDO ---
    }
}
