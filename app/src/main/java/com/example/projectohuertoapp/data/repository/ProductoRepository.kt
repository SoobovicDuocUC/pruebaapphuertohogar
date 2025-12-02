package com.example.projectohuertoapp.data.repository

import com.example.projectohuertoapp.model.Producto
import com.example.projectohuertoapp.network.ApiService
import com.example.projectohuertoapp.network.RetrofitClient

// 1. La Interfaz
interface ProductoRepository {
    suspend fun getProductos(): List<Producto>
}

// 2. La Implementación Real
// CAMBIO CLAVE: Ahora recibe 'apiService' en el constructor.
// Si no se lo pasamos, usa el de verdad (RetrofitClient.instance).
class NetworkProductoRepository(
    private val apiService: ApiService = RetrofitClient.instance
) : ProductoRepository {

    override suspend fun getProductos(): List<Producto> {
        return try {
            val respuesta = apiService.obtenerProductos()
            if (respuesta.isSuccessful) {
                respuesta.body() ?: emptyList()
            } else {
                // Podrías loguear el error aquí
                emptyList()
            }
        } catch (e: Exception) {
            // Error de conexión (sin internet, etc.)
            emptyList()
        }
    }
}