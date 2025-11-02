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
                currentList.map {
                    if (it.producto.id == producto.id) it.copy(cantidad = it.cantidad + 1) else it
                }
            } else {
                currentList + ItemCarrito(producto, 1)
            }
        }
    }

    fun eliminarDelCarrito(producto: Producto) {
        _items.update { currentList ->
            currentList.filter { it.producto.id != producto.id }
        }
    }

    fun decrementarCantidad(producto: Producto) {
        _items.update { currentList ->
            val existingItem = currentList.find { it.producto.id == producto.id }

            if (existingItem == null) {
                currentList
            } else if (existingItem.cantidad > 1) {
                currentList.map {
                    if (it.producto.id == producto.id) it.copy(cantidad = it.cantidad - 1) else it
                }
            } else {
                currentList.filter { it.producto.id != producto.id }
            }
        }
    }
}