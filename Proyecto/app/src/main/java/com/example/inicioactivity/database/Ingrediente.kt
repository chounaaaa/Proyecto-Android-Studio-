package com.example.inicioactivity.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(    tableName = "ingredientes",
    // Esta clave foránea asegura que cada ingrediente pertenece a una receta que existe.
    // Si una receta se borra, todos sus ingredientes se borrarán en cascada.
    foreignKeys = [ForeignKey(
        entity = Receta::class,
        parentColumns = ["id_receta"],
        childColumns = ["idReceta"], // El nombre debe coincidir con el campo de abajo
        onDelete = ForeignKey.CASCADE
    )]
)
data class Ingrediente(
    @PrimaryKey(autoGenerate = true)
    val id_ingrediente: Int = 0,
    val nombre: String,
    val cantidad: String,
    val idReceta: Int // Esta es la columna que conecta con la tabla de recetas
)
