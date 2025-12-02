package com.example.projectohuertoapp.model

data class AuthResponse(
    val token: String,
    val role: String,
    val email: String
)