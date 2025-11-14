package com.example.inicioactivity.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface IngredienteDao {
    // Usamos una lista porque es más eficiente insertar todos los ingredientes de una receta a la vez.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertarIngredientes(ingredientes: List<Ingrediente>)
    // ... (otras funciones)

    // Devuelve una lista de ingredientes que pertenecen a una receta específica.
    @Query("SELECT * FROM ingredientes WHERE idReceta = :recetaId")
    suspend fun obtenerIngredientesDeReceta(recetaId: Int): List<Ingrediente>

}
