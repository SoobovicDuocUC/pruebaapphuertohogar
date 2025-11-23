package com.example.projectohuertoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectohuertoapp.model.Producto
import com.example.projectohuertoapp.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CatalogoViewModel : ViewModel() {

    // Lista de productos que observará la vista
    private val _productos = MutableStateFlow<List<Producto>>(emptyList())
    val productos: StateFlow<List<Producto>> = _productos.asStateFlow()

    // Estado de carga (opcional, para mostrar un circulito girando)
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        // Cargar productos automáticamente al iniciar
        cargarProductos()
    }

    fun cargarProductos() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                println("HuertoApp: Iniciando conexión...")
                val respuesta = RetrofitClient.instance.obtenerProductos()

                if (respuesta.isSuccessful) {
                    val lista = respuesta.body() ?: emptyList()
                    _productos.value = lista
                    println("HuertoApp: Éxito! Se cargaron ${lista.size} productos.")
                } else {
                    println("HuertoApp: Error en la respuesta: ${respuesta.code()}")
                }
            } catch (e: Exception) {
                println("HuertoApp: Error de conexión: ${e.message}")
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}