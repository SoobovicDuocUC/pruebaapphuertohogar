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

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun registrar(usuario: Usuario, telefono: String) {
        viewModelScope.launch {
            try {
                // CORRECCIÓN: Usamos 'correo' y 'contrasena' (español) y la función del repo 'registrarUsuario'
                val resultado = repository.registrarUsuario(
                    nombre = usuario.nombre,
                    correo = usuario.correo,
                    contrasena = usuario.contrasena,
                )

                resultado.onSuccess { usuarioRegistrado ->
                    _usuarioLogueado.value = usuarioRegistrado
                    _error.value = null
                }.onFailure { exception ->
                    _error.value = exception.message ?: "Error al registrar"
                }
            } catch (e: Exception) {
                _error.value = "Error desconocido: ${e.message}"
            }
        }
    }

    fun login(email: String, clave: String) {
        viewModelScope.launch {
            try {
                // CORRECCIÓN: Llamamos a login en el repositorio
                val usuario = repository.login(email, clave)
                if (usuario != null) {
                    _usuarioLogueado.value = usuario
                    _error.value = null
                } else {
                    _error.value = "Credenciales incorrectas"
                }
            } catch (e: Exception) {
                _error.value = "Error al iniciar sesión"
            }
        }
    }

    fun logout() {
        _usuarioLogueado.value = null
    }
}