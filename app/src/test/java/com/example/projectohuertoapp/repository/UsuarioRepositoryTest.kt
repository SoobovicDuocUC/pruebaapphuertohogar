package com.example.projectohuertoapp.data.repository

import com.example.projectohuertoapp.model.AuthResponse
import com.example.projectohuertoapp.model.LoginRequest
import com.example.projectohuertoapp.model.Producto
import com.example.projectohuertoapp.model.RegisterRequest
import com.example.projectohuertoapp.network.ApiService
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response

// 1. API Falsa para simular el Backend de Usuarios
class FakeAuthApiService : ApiService {
    var debeFallarLogin = false
    var debeFallarRegistro = false

    // Simulamos respuesta exitosa
    private val successAuth = AuthResponse("fake_token_123", "USER", "test@correo.com")

    override suspend fun login(request: LoginRequest): Response<AuthResponse> {
        return if (debeFallarLogin) {
            Response.error(401, "Unauthorized".toResponseBody(null))
        } else {
            Response.success(successAuth)
        }
    }

    override suspend fun register(request: RegisterRequest): Response<AuthResponse> {
        return if (debeFallarRegistro) {
            Response.error(409, "Conflict".toResponseBody(null))
        } else {
            Response.success(successAuth)
        }
    }

    // No usamos productos en este test, retornamos vacío
    override suspend fun obtenerProductos(): Response<List<Producto>> = Response.success(emptyList())
}

class UsuarioRepositoryTest {

    @Test
    fun `login debe retornar Usuario cuando la API responde exitosamente`() = runTest {
        // GIVEN
        val fakeApi = FakeAuthApiService()
        val repository = UsuarioRepository(fakeApi)

        // WHEN
        val resultado = repository.login("test@correo.com", "123456")

        // THEN
        assertNotNull(resultado)
        assertEquals("test@correo.com", resultado?.correo)
    }

    @Test
    fun `login debe retornar null cuando la API falla (credenciales incorrectas)`() = runTest {
        // GIVEN
        val fakeApi = FakeAuthApiService()
        fakeApi.debeFallarLogin = true // Forzamos el error
        val repository = UsuarioRepository(fakeApi)

        // WHEN
        val resultado = repository.login("test@correo.com", "malapass")

        // THEN
        assertNull(resultado)
    }

    @Test
    fun `registrarUsuario debe retornar Success cuando la API responde ok`() = runTest {
        // GIVEN
        val fakeApi = FakeAuthApiService()
        val repository = UsuarioRepository(fakeApi)

        // WHEN
        val resultado = repository.registrarUsuario("Juan", "test@correo.com", "123")

        // THEN
        assertTrue(resultado.isSuccess)
        assertEquals("test@correo.com", resultado.getOrNull()?.correo)
    }
}