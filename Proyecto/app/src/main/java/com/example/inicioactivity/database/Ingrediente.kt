package com.example.inicioactivity.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "ingredientes",
    // Conecta cada ingrediente con la receta a la que pertenece.
    foreignKeys = [ForeignKey(
        entity = Receta::class,
        parentColumns = ["id_receta"],
        childColumns = ["id_receta_pertenece"],
        onDelete = ForeignKey.CASCADE // Si se borra una receta, se borran sus ingredientes.
    )]
)
data class Ingrediente(
    @PrimaryKey(autoGenerate = true)
    val id_ingrediente: Int = 0,
    val nombre: String,
    val cantidad: String, // Usamos String para ser flexibles (ej: "1 taza", "200 gr", "1 pizca")
    val id_receta_pertenece: Int // Columna que almacena el ID de la receta
)
