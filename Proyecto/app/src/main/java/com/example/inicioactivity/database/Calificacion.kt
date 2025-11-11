package com.example.inicioactivity.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date

/**
 * Representa una calificación (de 1 a 5 estrellas) que un usuario le da a una receta.
 *
 * @property id_calificacion Identificador único de la calificación.
 * @property id_receta El ID de la receta que está siendo calificada.
 * @property id_usuario El ID del usuario que está dando la calificación.
 * @property puntuacion El valor de la calificación, usualmente un número entero de 1 a 5.
 * @property fecha_calificacion La fecha en que se realizó la calificación.
 */
@Entity(
    tableName = "calificaciones",
    // Definimos las claves foráneas para mantener la integridad de los datos.
    foreignKeys = [
        ForeignKey(
            entity = Receta::class,
            parentColumns = ["id_receta"],
            childColumns = ["id_receta"],
            onDelete = ForeignKey.CASCADE // Si se borra la receta, se borran sus calificaciones.
        ),
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["id_usuario"],
            childColumns = ["id_usuario"],
            onDelete = ForeignKey.CASCADE // Si se borra el usuario, se borran sus calificaciones.
        )
    ]
)
data class Calificacion(
    @PrimaryKey(autoGenerate = true)
    val id_calificacion: Int = 0,

    val id_receta: Int,
    val id_usuario: Int,
    val puntuacion: Int, // Guardaremos un valor de 1, 2, 3, 4 o 5.
    val fecha_calificacion: Date = Date() // Se establece la fecha actual por defecto.
)
