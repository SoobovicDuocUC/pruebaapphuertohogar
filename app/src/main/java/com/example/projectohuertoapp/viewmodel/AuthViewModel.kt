package com.example.projectohuertoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectohuertoapp.data.local.entity.Usuario
import com.example.projectohuertoapp.data.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: UsuarioRepository) : ViewModel() {

    private val _usuarioLogueado = MutableStateFlow<Usuario?>(null)
    val usuarioLogueado: StateFlow<Usuario?> = _usuarioLogueado.asStateFlow()

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    private val _registroState = MutableStateFlow<RegistroState>(RegistroState.Idle)
    val registroState: StateFlow<RegistroState> = _registroState.asStateFlow()

    fun registrarUsuario(nombre: String, correo: String, contrasena: String) {
        viewModelScope.launch {
            _registroState.value = RegistroState.Loading
            val result = repository.registrarUsuario(nombre, correo, contrasena)
            result.fold(
                onSuccess = { usuario ->
                    _usuarioLogueado.value = usuario
                    _registroState.value = RegistroState.Success
                },
                onFailure = { error ->
                    _registroState.value = RegistroState.Error(error.message ?: "Error desconocido")
                }
            )
        }
    }

    fun iniciarSesion(correo: String, contrasena: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            val usuario = repository.login(correo, contrasena)
            if (usuario != null) {
                _usuarioLogueado.value = usuario
                _loginState.value = LoginState.Success
            } else {
                _loginState.value = LoginState.Error("Correo o contraseña incorrectos")
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