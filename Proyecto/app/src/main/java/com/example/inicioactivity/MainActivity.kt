package com.example.inicioactivity

import android.graphics.RenderEffect
import android.graphics.Shader // <-- 1. Asegúrate de que esta importación esté aquí
import android.os.Build
import android.os.Bundle
import android.widget.ImageView // <-- 2. Y esta también
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

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
    }
}
