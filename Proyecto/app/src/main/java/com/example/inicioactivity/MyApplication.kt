package com.example.inicioactivity

import android.app.Application
import com.example.inicioactivity.database.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyApplication : Application() {

    // --- ¡CAMBIO CLAVE! ---
    // Creamos una instancia "lazy" (perezosa) de la base de datos.
    // Esto significa que el código dentro de "lazy { ... }" solo se ejecutará
    // la primera vez que se acceda a la variable "database".
    // Esto nos garantiza un punto único y seguro de inicialización.
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }

    override fun onCreate() {
        super.onCreate()
        // Ahora que tenemos la instancia lazy, podemos forzar su inicialización
        // aquí mismo, en un hilo de fondo, para que el Callback se ejecute al inicio.
        CoroutineScope(Dispatchers.IO).launch {
            // Al acceder a "database" aquí, se ejecuta el código del "lazy"
            // y se crea la base de datos con su callback.
            database.usuarioDao() // Hacemos una llamada simple para asegurar la inicialización.
        }
    }
}
