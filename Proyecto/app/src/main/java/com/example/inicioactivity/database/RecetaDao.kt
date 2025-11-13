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

}
