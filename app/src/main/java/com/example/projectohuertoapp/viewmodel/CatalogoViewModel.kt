package com.example.projectohuertoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.projectohuertoapp.data.repository.ProductoRepository
import com.example.projectohuertoapp.model.Producto
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Aceptamos el repositorio y el dispatcher en el constructor
class CatalogoViewModel(
    private val repository: ProductoRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val _productos = MutableStateFlow<List<Producto>>(emptyList())
    val productos: StateFlow<List<Producto>> = _productos.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        cargarProductos()
    }

    fun cargarProductos() {
        viewModelScope.launch(ioDispatcher) { // Usamos el dispatcher inyectado
            _isLoading.value = true
            try {
                // Usamos el repositorio en lugar de Retrofit directo
                val lista = repository.getProductos()
                _productos.value = lista
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}

// Factory para crear el ViewModel con parámetros (igual que hiciste con AuthViewModel)
class CatalogoViewModelFactory(private val repository: ProductoRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CatalogoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CatalogoViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}