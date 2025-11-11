package com.example.inicioactivity.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// @Entity le dice a Room que esta clase representa una tabla en la base de datos.
@Entity(tableName = "usuarios")
data class Usuario(

    // @PrimaryKey indica que esta es la clave primaria.
    // autoGenerate = true hace que el ID se incremente automáticamente.
    @PrimaryKey(autoGenerate = true)
    val id_usuario: Int = 0,

    // @ColumnInfo permite personalizar el nombre de la columna.
    @ColumnInfo(name = "nombre_usuario")
    val nombreUsuario: String,

    @ColumnInfo(name = "correo")
    val correo: String,

    @ColumnInfo(name = "hash_contrasena")
    val hashContrasena: String,

    // Puedes añadir más campos de la tabla que diseñamos antes.
    @ColumnInfo(name = "fecha_registro")
    val fechaRegistro: Long = System.currentTimeMillis() // Guardar fechas como Long (milisegundos) es eficiente
)
