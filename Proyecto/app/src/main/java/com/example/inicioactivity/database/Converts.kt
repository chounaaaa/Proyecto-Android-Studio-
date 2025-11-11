package com.example.inicioactivity.database

import androidx.room.TypeConverter
import java.util.Date

/**
 * Esta clase le dice a Room cómo convertir tipos de datos complejos que no entiende
 * de forma nativa. En este caso, convierte objetos 'Date' a 'Long' y viceversa.
 */
class Converters {
    /**
     * Convierte un 'Long' (timestamp) de la base de datos a un objeto 'Date'.
     */
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        // Si el valor no es nulo, crea una nueva fecha a partir de él.
        return value?.let { Date(it) }
    }

    /**
     * Convierte un objeto 'Date' de tu código a un 'Long' (timestamp) para guardarlo en la base de datos.
     */
    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        // Si la fecha no es nula, obtiene su valor en milisegundos.
        return date?.time
    }
}
