package com.example.inicioactivity.database

import androidx.room.Entity
import androidx.room.PrimaryKey

// --- ¡SIMPLIFICADO! ---
// Se ha eliminado la sección de foreignKeys.
@Entity(tableName = "recetas")
data class Receta(
    @PrimaryKey(autoGenerate = true)
    val id_receta: Int = 0,
    val nombre: String,
    val descripcion: String,
    val tiempoPreparacion: Int,
    val dificultad: String,
    val pasos: String,
)
