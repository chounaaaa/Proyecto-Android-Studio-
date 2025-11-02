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

// ... (Código anterior sin cambios)

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        // ... (resto del código onCreate, incluyendo el ViewCompat y el blur)

        // 1. Encontrar los botones por su ID del XML.
        val registerButton = findViewById<MaterialButton>(R.id.buttonRegister)
        val loginButton = findViewById<MaterialButton>(R.id.button2) // Boton de "comenzar"

        // 🔘 CÓDIGO CORREGIDO PARA EL BOTÓN REGISTRAR
        // 2. Asignarle una acción para cuando se haga clic (Register).
        registerButton.setOnClickListener {
            // 3. Crear un Intent que apunte a la actividad Register.
            val intent = Intent(this, Register::class.java)
            // 4. Iniciar la nueva actividad.
            startActivity(intent)
        } // <-- ¡Cierre correcto del Listener de Register!

        // 🔘 CÓDIGO CORREGIDO PARA EL BOTÓN LOGIN ("Comenzar")
        // Este Listener debe ir separado e independiente.
        loginButton.setOnClickListener {
            // 3. Crear un Intent que apunte a la actividad Login.
            val intent = Intent(this, Login::class.java)

            // 4. Iniciar la nueva actividad.
            startActivity(intent)
        } // <-- ¡Cierre correcto del Listener de Login!

    } // <-- Cierre de onCreate()
} // <-- Cierre de la clase MainActivity (Ya no hay una llave extra al final del archivo)