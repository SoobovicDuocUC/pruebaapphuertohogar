package com.example.projectohuertoapp.data.repository
/*
import com.example.projectohuertoapp.data.local.UsuarioDao
import com.example.projectohuertoapp.data.local.entity.Usuario
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UsuarioRepository(private val usuarioDao: UsuarioDao) {

    /**
     * Intenta registrar un nuevo usuario.
     * Devuelve un Result que es Success si el correo no existe,
     * o Failure si el correo ya está registrado.
     */
    suspend fun registrarUsuario(nombre: String, correo: String, contrasena: String): Result<Usuario> = withContext(Dispatchers.IO) {
        try {
            // Verificar si el correo ya existe
            val usuarioExistente = usuarioDao.buscarPorCorreo(correo)
            if (usuarioExistente != null) {
                Result.failure(Exception("El correo electrónico ya está registrado."))
            } else {
                val nuevoUsuario = Usuario(nombre = nombre, correo = correo, contrasena = contrasena)
                usuarioDao.registrar(nuevoUsuario)
                // Buscamos el usuario recién insertado para obtener el ID
                val usuarioInsertado = usuarioDao.buscarPorCorreo(correo)!!
                Result.success(usuarioInsertado)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Intenta iniciar sesión.
     * Devuelve el Usuario si las credenciales son correctas, o null si no lo son.
     */
    suspend fun login(correo: String, contrasena: String): Usuario? = withContext(Dispatchers.IO) {
        usuarioDao.login(correo, contrasena)
    }
}

*/