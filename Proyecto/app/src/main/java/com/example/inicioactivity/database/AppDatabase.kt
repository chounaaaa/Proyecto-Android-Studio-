package com.example.inicioactivity.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Le decimos a Room qué entidades (tablas) contiene esta base de datos y la versión.
// Si añades más tablas (como Receta, Ingrediente), tienes que listarlas aquí.
@Database(entities = [Usuario::class, Receta::class, Ingrediente::class], version = 2)
abstract class AppDatabase : RoomDatabase() {

    // La base de datos debe exponer los DAOs.
    abstract fun usuarioDao(): UsuarioDao
    // abstract fun recetaDao(): RecetaDao // <-- Añadirías los otros DAOs aquí.

    companion object {
        // @Volatile asegura que el valor de INSTANCE sea siempre el más reciente.
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // synchronized evita que dos hilos creen la base de datos al mismo tiempo.
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "chefs_crew_database" // Nombre del archivo de la base de datos
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
