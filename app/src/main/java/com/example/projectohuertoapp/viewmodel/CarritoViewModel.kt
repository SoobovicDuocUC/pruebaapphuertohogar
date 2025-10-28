package com.example.projectohuertoapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.projectohuertoapp.model.ItemCarrito
import com.example.projectohuertoapp.model.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CarritoViewModel : ViewModel() {
    private val _items = MutableStateFlow<List<ItemCarrito>>(emptyList())
    val items = _items.asStateFlow()

    fun agregarAlCarrito(producto: Producto) {
        _items.update { currentList ->
            val existingItem = currentList.find { it.producto.id == producto.id }
            if (existingItem != null) {
                // Si ya existe, aumenta la cantidad
                currentList.map {
                    if (it.producto.id == producto.id) it.copy(cantidad = it.cantidad + 1) else it
                }
            } else {
                // Si no existe, lo añade a la lista
                currentList + ItemCarrito(producto, 1)
            }
        }
    }
}