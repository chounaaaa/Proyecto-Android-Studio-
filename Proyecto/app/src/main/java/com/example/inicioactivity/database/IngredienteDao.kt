package com.example.inicioactivity.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface IngredienteDao {
    // Usamos una lista porque es más eficiente insertar todos los ingredientes de una receta a la vez.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertarIngredientes(ingredientes: List<Ingrediente>)
}
