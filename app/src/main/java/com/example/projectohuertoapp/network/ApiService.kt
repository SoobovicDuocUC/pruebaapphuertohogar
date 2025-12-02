package com.example.projectohuertoapp.network

import com.example.projectohuertoapp.model.AuthResponse
import com.example.projectohuertoapp.model.LoginRequest
import com.example.projectohuertoapp.model.Producto
import com.example.projectohuertoapp.model.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    // Productos
    @GET("api/productos")
    suspend fun obtenerProductos(): Response<List<Producto>>

    // --- NUEVO: Autenticación ---
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>
}