package com.example.inicioactivity.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

// --- CORRECCIÓN AQUÍ ---
// Añadimos un índice único para la combinación de id_receta e id_usuario.
@Entity(
    tableName = "calificaciones",
    indices = [Index(value = ["id_receta", "id_usuario"], unique = true)], // <-- ESTA LÍNEA ES CLAVE
    foreignKeys = [
        ForeignKey(
            entity = Receta::class,
            parentColumns = ["id_receta"],
            childColumns = ["id_receta"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["id_usuario"],
            childColumns = ["id_usuario"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Calificacion(
    @PrimaryKey(autoGenerate = true)
    val id_calificacion: Int = 0,
    val id_receta: Int,
    val id_usuario: Int,
    val puntuacion: Int
)
