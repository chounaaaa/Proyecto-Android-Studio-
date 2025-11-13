package com.example.inicioactivity.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface RecetaDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertarReceta(receta: Receta): Long // Esto sigue siendo perfecto
}
