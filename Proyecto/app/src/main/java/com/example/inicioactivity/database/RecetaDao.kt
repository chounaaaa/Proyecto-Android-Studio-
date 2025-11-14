package com.example.inicioactivity.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RecetaDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertarReceta(receta: Receta): Long // Esto sigue siendo perfecto

    // --- ¡NUEVA FUNCIÓN! ---
    // Cuenta cuántas filas hay en la tabla de recetas.
    @Query("SELECT COUNT(*) FROM recetas")
    suspend fun contarRecetas(): Int

    @Query("SELECT * FROM recetas WHERE nombre = :nombreReceta LIMIT 1")
    suspend fun buscarPorNombre(nombreReceta: String): Receta? // El '?' es importante

    @Query("SELECT * FROM recetas")
    suspend fun obtenerTodas(): List<Receta>

    // Devuelve una única receta basada en su ID.
    @Query("SELECT * FROM recetas WHERE id_receta = :id")
    suspend fun obtenerPorId(id: Int): Receta? // Hacemos que pueda ser nulo por si no la encuentra

    /**
     * Obtiene todas las recetas junto con su calificación promedio.
     * Utiliza un LEFT JOIN para asegurarse de que se incluyan TODAS las recetas,
     * incluso si no tienen ninguna calificación en la tabla 'calificaciones'.
     * GROUP BY agrupa los resultados por receta para calcular el AVG() correctamente.
     */
    @Query("""
        SELECT 
            recetas.*, 
            AVG(calificaciones.puntuacion) as promedioCalificacion 
        FROM 
            recetas 
        LEFT JOIN 
            calificaciones ON recetas.id_receta = calificaciones.id_receta 
        GROUP BY 
            recetas.id_receta
    """)
    suspend fun obtenerRecetasConPromedio(): List<RecetaConPromedio>
}
