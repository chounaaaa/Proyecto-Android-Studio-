package com.example.inicioactivity.database

import androidx.room.Embedded

// Esta clase no es una tabla, es un objeto de datos personalizado para los resultados de una consulta.
data class RecetaConPromedio(
    // Incrusta todos los campos de la clase Receta (id_receta, nombre, etc.)
    @Embedded
    val receta: Receta,

    // Añade un campo extra para la calificación promedio.
    // Lo hacemos Double? para que pueda ser nulo si no hay calificaciones.
    val promedioCalificacion: Double?
)
