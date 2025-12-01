package com.example.projectohuertoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectohuertoapp.network.dto.UsuarioDTO
import com.example.projectohuertoapp.network.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: UsuarioRepository) : ViewModel() {

    // El estado del usuario logueado ahora es de tipo UsuarioDTO
    private val _usuarioLogueado = MutableStateFlow<UsuarioDTO?>(null)
    val usuarioLogueado: StateFlow<UsuarioDTO?> = _usuarioLogueado.asStateFlow()

    val isLoggedIn: StateFlow<Boolean> = usuarioLogueado.map { it != null }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    private val _registroState = MutableStateFlow<RegistroState>(RegistroState.Idle)
    val registroState: StateFlow<RegistroState> = _registroState.asStateFlow()

    /**
     * Llama al Repositorio para registrar un nuevo usuario en el servidor.
     */
    fun registrarUsuario(nombre: String, correo: String, contrasena: String) {
        viewModelScope.launch {
            _registroState.value = RegistroState.Loading
            // El Repositorio manejará la conversión de (nombre -> username) y la llamada a Retrofit
            val result = repository.registrarUsuario(nombre, correo, contrasena)
            result.fold(
                onSuccess = { usuarioDB ->
                    // Guardamos el UsuarioDTO devuelto por el servidor
                    _usuarioLogueado.value = usuarioDB
                    _registroState.value = RegistroState.Success
                },
                onFailure = { error ->
                    _registroState.value = RegistroState.Error(error.message ?: "Error desconocido")
                }
            )
        }
    }

    /**
     * Llama al Repositorio para iniciar sesión con email y contraseña.
     */
    fun iniciarSesion(correo: String, contrasena: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            // El Repositorio llama a Retrofit para autenticar
            val usuario = repository.login(correo, contrasena)
            if (usuario != null) {
                // Guardamos el UsuarioDTO si el login fue exitoso
                _usuarioLogueado.value = usuario
                _loginState.value = LoginState.Success
            } else {
                _loginState.value = LoginState.Error("Correo o contraseña incorrectos o error de red")
            }
        }
    }

    fun cerrarSesion() {
        _usuarioLogueado.value = null
        _loginState.value = LoginState.Idle
        _registroState.value = RegistroState.Idle
    }

    fun resetLoginState() {
        _loginState.value = LoginState.Idle
    }

    fun resetRegistroState() {
        _registroState.value = RegistroState.Idle
    }
}

// --- CLASES DE ESTADO (Sin Cambios) ---

sealed class LoginState {
    data object Idle : LoginState()
    data object Loading : LoginState()
    data object Success : LoginState()
    data class Error(val message: String) : LoginState()
}

sealed class RegistroState {
    data object Idle : RegistroState()
    data object Loading : RegistroState()
    data object Success : RegistroState()
    data class Error(val message: String) : RegistroState()
}