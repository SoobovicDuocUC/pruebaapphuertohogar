package com.example.projectohuertoapp.network.repository

import com.example.projectohuertoapp.network.ApiService
import com.example.projectohuertoapp.network.dto.UsuarioDTO

class UsuarioRepository(
    private val api: ApiService // <-- Inyectar la API de Retrofit
    // private val dao: UsuarioDao <-- Eliminar la dependencia de Room DAO
) {
    // --- FUNCIÓN DE REGISTRO ---
    // Usamos Result para manejar errores de red/servidor fácilmente
    suspend fun registrarUsuario(nombre: String, correo: String, contrasena: String): Result<UsuarioDTO> {
        val registroDTO = UsuarioDTO(
            email = correo,
            username = nombre, // 'nombre' del frontend se mapea a 'username' del backend
            password = contrasena
        )

        return try {
            val response = api.register(registroDTO)
            if (response.isSuccessful && response.body() != null) {
                // Éxito: Retornar el usuario registrado
                Result.success(response.body()!!)
            } else {
                // Error de servidor (ej. 409 Conflict por email duplicado)
                Result.failure(Exception(response.errorBody()?.string() ?: "Error de registro"))
            }
        } catch (e: Exception) {
            // Error de conexión o JSON
            Result.failure(e)
        }
    }

    // --- FUNCIÓN DE LOGIN ---
    suspend fun login(correo: String, contrasena: String): UsuarioDTO? {
        val loginDTO = UsuarioDTO(
            email = correo,
            username = "", // No se usa para el login, pero debe estar en el DTO si es obligatorio
            password = contrasena
        )

        return try {
            val response = api.login(loginDTO)
            if (response.isSuccessful && response.body() != null) {
                return response.body() // Retorna el usuario logueado
            }
            // Si no es successful (ej. 401 Unauthorized), retorna null
            return null
        } catch (e: Exception) {
            // Error de red
            return null
        }
    }
}