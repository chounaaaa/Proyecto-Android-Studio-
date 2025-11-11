package com.example.inicioactivity.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "recetas",
    // Esta es la clave para conectar una receta con el usuario que la creó.
    foreignKeys = [ForeignKey(
        entity = Usuario::class,
        parentColumns = ["id_usuario"],
        childColumns = ["id_usuario_creador"],
        onDelete = ForeignKey.CASCADE // Si se borra un usuario, se borran sus recetas.
    )]
)
data class Receta(
    @PrimaryKey(autoGenerate = true)
    val id_receta: Int = 0,
    val nombre: String,
    val descripcion: String,
    val tiempoPreparacion: Int, // En minutos
    val dificultad: String,     // Ej: "Fácil", "Media", "Difícil"
    val id_usuario_creador: Int // Columna que almacena el ID del usuario
)
