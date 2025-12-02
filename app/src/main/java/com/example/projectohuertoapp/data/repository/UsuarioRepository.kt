package com.example.projectohuertoapp.data.repository

import com.example.projectohuertoapp.data.local.entity.Usuario
import com.example.projectohuertoapp.model.LoginRequest
import com.example.projectohuertoapp.model.RegisterRequest
import com.example.projectohuertoapp.network.ApiService
import com.example.projectohuertoapp.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// Ahora recibe 'apiService' para poder inyectar el Fake en los tests
class UsuarioRepository(
    private val apiService: ApiService = RetrofitClient.instance
) {

    suspend fun registrarUsuario(nombre: String, correo: String, contrasena: String): Result<Usuario> = withContext(Dispatchers.IO) {
        try {
            val request = RegisterRequest(email = correo, password = contrasena)
            val response = apiService.register(request)

            if (response.isSuccessful && response.body() != null) {
                // Convertimos la respuesta del backend a tu entidad Usuario local
                val authResponse = response.body()!!
                val nuevoUsuario = Usuario(
                    nombre = nombre,
                    correo = authResponse.email,
                    contrasena = "" // Por seguridad no guardamos la pass plana
                )
                Result.success(nuevoUsuario)
            } else {
                Result.failure(Exception("Error en registro: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(correo: String, contrasena: String): Usuario? = withContext(Dispatchers.IO) {
        try {
            val request = LoginRequest(email = correo, password = contrasena)
            val response = apiService.login(request)

            if (response.isSuccessful && response.body() != null) {
                val authResponse = response.body()!!
                // Retornamos un usuario válido
                Usuario(
                    nombre = "Usuario", // El backend no devuelve nombre, solo email/rol
                    correo = authResponse.email,
                    contrasena = ""
                )
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}