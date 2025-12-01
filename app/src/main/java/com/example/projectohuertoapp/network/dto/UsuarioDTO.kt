package com.example.projectohuertoapp.network.dto

data class UsuarioDTO(
    val id: Long? = null,
    val email: String,
    val username: String? = null, // <--- NUEVO: Opcional, solo necesario en el registro
    val password: String,
    val role: String? = null
)