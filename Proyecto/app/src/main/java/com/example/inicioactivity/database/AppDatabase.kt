package com.example.inicioactivity.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
// --- BORRADO --- Ya no necesitamos estas importaciones aquí
// import androidx.sqlite.db.SupportSQLiteDatabase
// import com.example.inicioactivity.MyApplication
// import kotlinx.coroutines.CoroutineScope
// import kotlinx.coroutines.Dispatchers
// import kotlinx.coroutines.launch

// La versión ya no importa tanto, pero la dejamos. Puedes ponerla en 1 si quieres.
@Database(entities = [Usuario::class, Receta::class, Ingrediente::class, Calificacion::class], version = 14) // Sube la versión por última vez
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao
    abstract fun recetaDao(): RecetaDao
    abstract fun ingredienteDao(): IngredienteDao
    abstract fun calificacionDao(): CalificacionDao
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
                    .fallbackToDestructiveMigration()
                    // --- ¡CAMBIO CLAVE! BORRAMOS EL CALLBACK ---
                    // .addCallback(...)
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }

    // --- ¡CAMBIO CLAVE! BORRAMOS TODA LA CLASE DATABASECALLBACK Y LA FUNCIÓN rellenarDatosIniciales DE AQUÍ ---
}
