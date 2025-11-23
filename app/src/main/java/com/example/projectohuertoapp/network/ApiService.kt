package com.example.projectohuertoapp.network

import com.example.projectohuertoapp.model.Producto
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    // Esta ruta "/api/productos" debe coincidir con tu ProductoController de Java
    @GET("api/productos")
    suspend fun obtenerProductos(): Response<List<Producto>>
}