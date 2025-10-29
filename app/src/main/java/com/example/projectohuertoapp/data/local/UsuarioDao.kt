package com.example.projectohuertoapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.projectohuertoapp.data.local.entity.Usuario

@Dao
interface UsuarioDao {

    /**
     * Inserta un nuevo usuario en la base de datos.
     * La anotación @Insert se encarga de generar el SQL.
     */
    @Insert
    suspend fun registrar(usuario: Usuario)

    /**
     * Busca un usuario por su correo electrónico.
     * Se usa para verificar si un correo ya está registrado.
     */
    @Query("SELECT * FROM usuarios WHERE correo = :correo LIMIT 1")
    suspend fun buscarPorCorreo(correo: String): Usuario?

    /**
     * Busca un usuario que coincida con el correo Y la contraseña.
     * Se usa para el login.
     */
    @Query("SELECT * FROM usuarios WHERE correo = :correo AND contrasena = :contrasena LIMIT 1")
    suspend fun login(correo: String, contrasena: String): Usuario?
}
