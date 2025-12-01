package com.example.projectohuertoapp.network

import com.example.projectohuertoapp.model.Producto
import com.example.projectohuertoapp.network.dto.UsuarioDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    // Esta ruta "/api/productos" debe coincidir con tu ProductoController de Java
    @GET("api/productos")
    suspend fun obtenerProductos(): Response<List<Producto>>

    // Endpoint de Registro
    @POST("/api/usuarios/register")
    suspend fun register(@Body usuario: UsuarioDTO): Response<UsuarioDTO>

    // Endpoint de Login
    @POST("/api/usuarios/login")
    // Se usa el mismo DTO, pero solo se envían email y password
    suspend fun login(@Body usuario: UsuarioDTO): Response<UsuarioDTO>

}