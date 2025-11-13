package com.example.inicioactivity

import android.app.Application
import com.example.inicioactivity.database.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyApplication : Application() {

    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }

    override fun onCreate() {
        super.onCreate()

        // Lanzamos una corutina para hacer la comprobación en segundo plano
        CoroutineScope(Dispatchers.IO).launch {
            // 1. Obtenemos el Dao de recetas
            val recetaDao = database.recetaDao()

            // Ya no comprobamos si la tabla está vacía.
            // Simplemente llamamos a la función que se encarga de todo.
            rellenarDatosIniciales(database.recetaDao(), database.ingredienteDao())

            // Si no está vacía, no hacemos nada. Los datos de usuario se conservan.
        }
    }

    // --- LÓGICA MOVIDA AQUÍ ---
    private suspend fun rellenarDatosIniciales(recetaDao: RecetaDao, ingredienteDao: IngredienteDao) {
        // --- Comprobación para Receta 1: Chorizo a la Pomarola ---
        if (recetaDao.buscarPorNombre("Chorizo a la Pomarola") == null) {
            // Si no existe, la creamos
            val chorizoALaPomarola = Receta(
                id_receta = 0,
                nombre = "Chorizo a la Pomarola",
                descripcion = "Un guiso que contiene chorizo y un rico tuco.",
                tiempoPreparacion = 50,
                dificultad = "Difícil",
                pasos = "..." // (tu texto completo de los pasos del chorizo)
            )
            val idRecetaChorizo = recetaDao.insertarReceta(chorizoALaPomarola).toInt()
            val ingredientesChorizo = listOf(
                Ingrediente(nombre = "chorizos", cantidad = "4 unidades", idReceta = idRecetaChorizo),
                // ... (resto de ingredientes del chorizo)
            )
            ingredienteDao.insertarIngredientes(ingredientesChorizo)
        }

        // --- Comprobación para Receta 2: Empanadas de matambre criollas ---
        if (recetaDao.buscarPorNombre("Empanadas de matambre criollas") == null) {
            // Si no existe, la creamos
            val empanadasDeMatambre = Receta(
                id_receta = 0,
                nombre = "Empanadas de matambre criollas",
                descripcion = "Clásicas empanadas argentinas con un relleno jugoso y sabroso de matambre.",
                tiempoPreparacion = 150, // Sumando cocción de matambre, relleno y horno
                dificultad = "Difícil",
                pasos = """
                1. Colocamos el matambre en una olla donde quepa cómodamente. Cubrimos con agua y agregamos la leche. Salpimentamos y cocinamos a fuego bajo por una hora.
                2. Transcurrido del tiempo de cocción retiramos de la olla y dejamos a enfriar. Desgrasamos sin dejar totalmente magro y cortamos en cubitos de 1 cm de lado. Reservamos.
                3. En otra olla colocamos apenas un hilo de aceite y calentamos por unos segundos. Añadimos la cebolla y el morrón cortado en cubitos. Cocinamos hasta transparentar.
                4. Añadimos el matambre cortado y las pasas de uva. Sumamos el ají molido, el pimentón y cocinamos por unos 30 minutos a fuego bajo a olla destapada, para que se consuman los líquidos.
                5. Retiramos del fuego y sumamos los huevos duros picados y las aceitunas también picadas. Mezclamos y dejamos enfriar.
                6. Una vez frío el relleno, colocamos una porción en el centro de cada tapa. Humedecemos el borde, cerramos y hacemos el repulgue.
                7. Cocinamos en horno precalentado fuerte (200°C) hasta que estén doradas, aproximadamente unos 40 minutos.
            """.trimIndent()
            )
            val idRecetaEmpanadas = recetaDao.insertarReceta(empanadasDeMatambre).toInt()
            val ingredientesEmpanadas = listOf(
                Ingrediente(nombre = "Matambre vacuno", cantidad = "700g", idReceta = idRecetaEmpanadas),
                Ingrediente(nombre = "Leche (para cocción)", cantidad = "100ml", idReceta = idRecetaEmpanadas),
                Ingrediente(nombre = "Cebolla", cantidad = "500g", idReceta = idRecetaEmpanadas),
                Ingrediente(nombre = "Morrón rojo", cantidad = "200g", idReceta = idRecetaEmpanadas),
                Ingrediente(nombre = "Pasas de uva", cantidad = "50g", idReceta = idRecetaEmpanadas),
                Ingrediente(nombre = "Huevos duros", cantidad = "5 unidades", idReceta = idRecetaEmpanadas),
                Ingrediente(nombre = "Aceitunas verdes", cantidad = "7 unidades", idReceta = idRecetaEmpanadas),
                Ingrediente(nombre = "Tapas de empanadas hojaldradas", cantidad = "12 unidades", idReceta = idRecetaEmpanadas),
                Ingrediente(nombre = "Ají molido y pimentón", cantidad = "a gusto", idReceta = idRecetaEmpanadas),
                Ingrediente(nombre = "Sal y pimienta", cantidad = "a gusto", idReceta = idRecetaEmpanadas),
                Ingrediente(nombre = "Aceite", cantidad = "cantidad necesaria", idReceta = idRecetaEmpanadas)
            )
            ingredienteDao.insertarIngredientes(ingredientesEmpanadas)
        }
        // Si mañana quieres añadir otra receta, la pones aquí.
    }
}
