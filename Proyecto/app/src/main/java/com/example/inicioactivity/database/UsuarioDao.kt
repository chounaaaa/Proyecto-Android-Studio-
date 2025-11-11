package com.example.inicioactivity.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

// @Dao le dice a Room que esto es un Data Access Object.
@Dao
interface UsuarioDao {

    // suspend significa que esta función se debe llamar desde una coroutine (hilo secundario).
    // OnConflict = IGNORE evita que la app crashee si intentas insertar un usuario que ya existe.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertarUsuario(usuario: Usuario)

    // @Query te permite hacer consultas SQL. Room las revisa para que no tengan errores.
    @Query("SELECT * FROM usuarios WHERE nombre_usuario = :nombreUsuario")
    suspend fun getUsuarioPorNombre(nombreUsuario: String): Usuario?

    @Query("SELECT * FROM usuarios WHERE correo = :correo")
    suspend fun getUsuarioPorCorreo(correo: String): Usuario?
}
