package com.example.projectohuertoapp.data.repository

import com.example.projectohuertoapp.network.ApiService
import com.example.projectohuertoapp.network.dto.UsuarioDTO
import com.example.projectohuertoapp.network.repository.UsuarioRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.*
import org.junit.Test
import retrofit2.Response

class UsuarioRepositoryTest {

    // 1. Instancia los mocks aquí como propiedades de la clase
    private val api: ApiService = mockk()
    private val repository = UsuarioRepository(api)

    // Datos de prueba
    private val mockUsuarioDTO = UsuarioDTO(
        id = 1L,
        email = "test@huerto.com",
        username = "HuertoTest",
        password = "pass123",
        role = "USER"
    )

    @Test
    fun registrarUsuario_deberia_funcionar_si_API_responde_200() = runBlocking {
        coEvery { api.register(any()) } returns Response.success(mockUsuarioDTO)

        val result = repository.registrarUsuario("HuertoTest", "test@huerto.com", "pass123")

        assertTrue(result.isSuccess)
        assertEquals(mockUsuarioDTO, result.getOrNull())
    }

    @Test
    fun registrarUsuario_deberia_fallar_si_API_responde_error() = runBlocking {
        coEvery { api.register(any()) } returns Response.error(
            409,
            "Email duplicado".toResponseBody("application/json".toMediaTypeOrNull())
        )

        val result = repository.registrarUsuario("Duplicate", "test@huerto.com", "pass123")

        assertTrue(result.isFailure)
    }
}
