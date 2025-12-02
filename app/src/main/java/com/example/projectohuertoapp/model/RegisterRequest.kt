package com.example.projectohuertoapp.model

data class RegisterRequest(
    val email: String,
    val password: String,
    val role: String = "USER"
)