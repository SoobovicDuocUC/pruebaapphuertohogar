package com.example.projectohuertoapp.viewmodel

import com.example.projectohuertoapp.network.dto.UsuarioDTO
import com.example.projectohuertoapp.network.repository.UsuarioRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {

    // 1. Mocks y Dispatchers
    private lateinit var repository: UsuarioRepository
    private lateinit var viewModel: AuthViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    // 2. Datos de prueba
    private val mockUsuarioDTO = UsuarioDTO(
        id = 1L,
        email = "test@huerto.com",
        username = "HuertoTest",
        password = "pass",
        role = "USER"
    )

    // 3. Configuración inicial (@Before)
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher) // Reemplazar el Main Dispatcher para viewModelScope
        repository = mockk()
        viewModel = AuthViewModel(repository)
    }

    // 4. Limpieza (@After)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // --- TESTS: REGISTRAR USUARIO ---

    @Test
    fun `registrarUsuario deberia transicionar a Success y guardar el usuario en exito`() = runTest {

        val nombre = "NewUser"
        val correo = "new@test.com"
        val pass = "secure"
        coEvery { repository.registrarUsuario(any(), any(), any()) } returns Result.success(mockUsuarioDTO)

        viewModel.registrarUsuario(nombre, correo, pass)

        assertEquals(RegistroState.Success, viewModel.registroState.value)
        assertEquals(mockUsuarioDTO, viewModel.usuarioLogueado.value)
        assertTrue(viewModel.isLoggedIn.value)
    }

    @Test
    fun `registrarUsuario deberia transicionar a Error en caso de fallo del repositorio`() = runTest {

        val errorMessage = "Email ya registrado"
        coEvery { repository.registrarUsuario(any(), any(), any()) } returns Result.failure(Exception(errorMessage))


        viewModel.registrarUsuario("NewUser", "new@test.com", "secure")


        assertEquals(RegistroState.Error(errorMessage), viewModel.registroState.value)
        assertNull(viewModel.usuarioLogueado.value)
        assertFalse(viewModel.isLoggedIn.value)
    }

    // --- TESTS: INICIAR SESIÓN ---

    @Test
    fun `iniciarSesion deberia transicionar a Success y guardar el usuario en credenciales correctas`() = runTest {
        coEvery { repository.login(any(), any()) } returns mockUsuarioDTO

        viewModel.iniciarSesion("user@test.com", "pass")

        assertEquals(LoginState.Success, viewModel.loginState.value)
        assertEquals(mockUsuarioDTO, viewModel.usuarioLogueado.value)
    }

    @Test
    fun `iniciarSesion deberia transicionar a Error en credenciales incorrectas`() = runTest {
        coEvery { repository.login(any(), any()) } returns null

        viewModel.iniciarSesion("wrong@test.com", "wrongpass")

        val estadoActual = viewModel.loginState.value
        assertTrue(estadoActual is LoginState.Error)
        assertEquals("Correo o contraseña incorrectos", (estadoActual as LoginState.Error).message)
        assertNull(viewModel.usuarioLogueado.value)
    }

}
