package com.example.inicioactivity.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters // ¡Necesitas esta importación para las fechas!

// ¡AÑADE TODAS TUS TABLAS Y AUMENTA LA VERSIÓN!
// Si ya creaste Calificacion.kt, etc., añádelas aquí.
// La versión debe ser MAYOR a la que tenías. Si era 1, pon 2.
@Database(entities = [Usuario::class, Receta::class, Ingrediente::class, Calificacion::class], version = 4)
@TypeConverters(Converters::class) // ¡Añade esto para que Room guarde las fechas!
abstract class AppDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "chefs_crew_database"
                )
                    // --- ¡ESTA ES LA LÍNEA MÁGICA QUE FALTABA! ---
                    // Le dice a Room que si hay un cambio de versión,
                    // destruya la base de datos vieja y cree una nueva.
                    // ¡Esto borra todos los datos, pero arregla el problema!
                    .fallbackToDestructiveMigration()
                    .build() // Ahora el .build() está después.

                INSTANCE = instance
                instance
            }
        }
    }
}
