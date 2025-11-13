package com.example.inicioactivity.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.inicioactivity.MyApplication // Asegúrate de importar tu clase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// La versión es 8, está bien.
@Database(entities = [Usuario::class, Receta::class, Ingrediente::class, Calificacion::class], version = 8)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao
    abstract fun recetaDao(): RecetaDao
    abstract fun ingredienteDao(): IngredienteDao

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
                    // --- ¡CAMBIO CLAVE! Pasamos la instancia de Application al Callback ---
                    .addCallback(DatabaseCallback(context.applicationContext as MyApplication))
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }

    // --- ¡CAMBIO CLAVE! El Callback ahora recibe MyApplication ---
    private class DatabaseCallback(private val application: MyApplication) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            CoroutineScope(Dispatchers.IO).launch {
                // --- ¡SOLUCIÓN! Usamos la instancia 'lazy' que ya existe en MyApplication ---
                val recetaDao = application.database.recetaDao()
                val ingredienteDao = application.database.ingredienteDao()
                rellenarDatosIniciales(recetaDao, ingredienteDao)
            }
        }

        suspend fun rellenarDatosIniciales(recetaDao: RecetaDao, ingredienteDao: IngredienteDao) {
            // **1. Crear el objeto Receta**
            val chorizoALaPomarola = Receta(
                id_receta = 0,
                nombre = "Chorizo a la Pomarola",
                descripcion = "Un guiso que contiene chorizo y un rico tuco.",
                tiempoPreparacion = 50,
                dificultad = "Difícil",
                pasos = """
                    1. En una sartén grande(u olla), calienta el aceite de oliva a fuego medio.
                    2. Agregá los chorizos doralos por todos lados, aproximadamente 5-7 minutos, luego sacalos de la sarten y reservamos.
                    3. En la misma sartén,(para tomar los jugos y/o restos de chorizo) agrega la cebolla picada y el pimiento rojo. Deben permanecer allí hasta que la cebolla esté transparente y el pimiento esté tierno, unos 5 minutos.
                    4. Sumale ajo picado y cocina por un minuto más, teniendo cuidado de no quemarlo.
                    5. Incorpora el tomate triturado y la hoja de laurel. Añade una cucharada de azúcar para balancear la acidez del tomate, luego echas sal y salpimienta al gusto.
                    6. Cocina a fuego medio-bajo durante unos 10 minutos, removiendo de vez en cuando, hasta que la salsa se haya espesado un poco.
                    7. Vuelve a colocar los chorizos en la sartén y asegúrate de que estén bien cubiertos con la salsa. Cocina a fuego lento durante unos 5 minutos más para que los sabores se integren.
                    8. Retira la hoja de laurel antes de servir y, si lo deseas, espolvorea con perejil fresco picado.
                """.trimIndent()
            )

            // **2. Insertar la receta y OBTENER SU ID REAL**
            val idRecetaNueva = recetaDao.insertarReceta(chorizoALaPomarola).toInt()

            // **3. Crear la lista de ingredientes usando el ID REAL**
            val ingredientesChorizoALaPomarola = listOf(
                Ingrediente(nombre = "chorizos", cantidad = "4 unidades", idReceta = idRecetaNueva),
                Ingrediente(nombre = "cebolla mediana, picada", cantidad = "1 unidad", idReceta = idRecetaNueva),
                Ingrediente(nombre = "dientes de ajo, picados", cantidad = "2 unidades", idReceta = idRecetaNueva),
                Ingrediente(nombre = "pimiento rojo, cortado en tiras", cantidad = "1 unidad", idReceta = idRecetaNueva),
                Ingrediente(nombre = "tomate triturado (enlatado o fresco)", cantidad = "400g", idReceta = idRecetaNueva),
                Ingrediente(nombre = "cucharada de azúcar", cantidad = "1 cucharadita", idReceta = idRecetaNueva),
                Ingrediente(nombre = "sal y pimienta", cantidad = "a gusto", idReceta = idRecetaNueva),
                Ingrediente(nombre = "aceite de oliva", cantidad = "2 cucharada", idReceta = idRecetaNueva),
                Ingrediente(nombre = "hoja de laurel", cantidad = "1 hoja", idReceta = idRecetaNueva),
                Ingrediente(nombre = "perejil fresco picado", cantidad = "(opcional)", idReceta = idRecetaNueva)
            )

            // **4. Insertar la lista de ingredientes**
            ingredienteDao.insertarIngredientes(ingredientesChorizoALaPomarola)
        }
    }
}
