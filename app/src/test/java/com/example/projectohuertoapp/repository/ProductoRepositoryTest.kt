package com.example.projectohuertoapp.data.repository

import com.example.projectohuertoapp.model.AuthResponse
import com.example.projectohuertoapp.model.LoginRequest
import com.example.projectohuertoapp.model.Producto
import com.example.projectohuertoapp.model.RegisterRequest
import com.example.projectohuertoapp.network.ApiService
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response

// 1. API Falsa para pruebas (Simula ser Retrofit)
class FakeApiService : ApiService {
    // Variable para controlar si queremos que la "API" falle o tenga éxito
    var debeFallar = false
    var datosDePrueba = listOf<Producto>()

    override suspend fun obtenerProductos(): Response<List<Producto>> {
        return if (debeFallar) {
            // Simulamos un error 404 Not Found
            Response.error(404, "No encontrado".toResponseBody(null))
        } else {
            // Simulamos éxito 200 OK con datos
            Response.success(datosDePrueba)
        }
    }

    // --- AGREGAMOS ESTAS FUNCIONES PARA QUE NO DE ERROR ---
    // (No las usamos en este test, así que pueden devolver success vacío o error)
    override suspend fun login(request: LoginRequest): Response<AuthResponse> {
        return Response.success(AuthResponse("token", "USER", "email"))
    }

    override suspend fun register(request: RegisterRequest): Response<AuthResponse> {
        return Response.success(AuthResponse("token", "USER", "email"))
    }
}

class ProductoRepositoryTest {

    @Test
    fun `getProductos debe retornar lista cuando la API responde exitosamente`() = runTest {
        // GIVEN (Dado)
        val productoPrueba = Producto(1, "COD1", "Manzana Test", 1000, "url", "Fruta", "1kg", "Desc")
        val fakeApi = FakeApiService()
        fakeApi.datosDePrueba = listOf(productoPrueba) // Le damos datos a la API falsa

        // Inyectamos la API falsa en el repositorio
        val repository = NetworkProductoRepository(fakeApi)

        // WHEN (Cuando)
        val resultado = repository.getProductos()

        // THEN (Entonces)
        assertEquals(1, resultado.size)
        assertEquals("Manzana Test", resultado[0].nombre)
    }

    @Test
    fun `getProductos debe retornar lista vacia cuando la API falla`() = runTest {
        // GIVEN (Dado)
        val fakeApi = FakeApiService()
        fakeApi.debeFallar = true // Configuramos la API para que falle

        val repository = NetworkProductoRepository(fakeApi)

        // WHEN (Cuando)
        val resultado = repository.getProductos()

        // THEN (Entonces)
        assertTrue(resultado.isEmpty()) // Debería devolver lista vacía para no romper la UI
    }
}