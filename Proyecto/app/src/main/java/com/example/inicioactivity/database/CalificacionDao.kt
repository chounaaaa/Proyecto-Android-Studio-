package com.example.inicioactivity.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CalificacionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(calificacion: Calificacion)

    // --- CORRECCIÓN AQUÍ ---
    // Cambia Float? por Double?
    // AVG() en SQL devuelve un tipo de dato que Room mapea a Double.
    @Query("SELECT AVG(puntuacion) FROM calificaciones WHERE id_Receta = :recetaId")
    suspend fun obtenerPromedioReceta(recetaId: Int): Double?

    @Query("SELECT * FROM calificaciones WHERE id_Receta = :recetaId AND id_Usuario = :usuarioId")
    suspend fun obtenerCalificacionUsuario(recetaId: Int, usuarioId: Int): Calificacion?
}
