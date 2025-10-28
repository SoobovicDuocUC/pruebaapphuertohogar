package com.example.projectohuertoapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.projectohuertoapp.R // <-- IMPORTANTE
import com.example.projectohuertoapp.model.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CatalogoViewModel : ViewModel() {

    private val _productos = MutableStateFlow<List<Producto>>(emptyList())
    val productos: StateFlow<List<Producto>> = _productos.asStateFlow()

    init {
        cargarProductosDeEjemplo()
    }

    private fun cargarProductosDeEjemplo() {
        _productos.value = listOf(
            // Reemplaza 'R.drawable.placeholder' con tus imágenes reales.
            Producto("FR001", "Manzanas Fuji", "Crujientes y dulces, del Valle del Maule.", 1200.0, 150, "Frutas Frescas", R.drawable.ic_launcher_background),
            Producto("VR001", "Zanahorias Orgánicas", "Cultivadas sin pesticidas en O'Higgins.", 900.0, 100, "Verduras Orgánicas", R.drawable.ic_launcher_background),
            Producto("PO001", "Miel Orgánica", "Frasco de 500g de apicultores locales.", 5000.0, 50, "Productos Orgánicos", R.drawable.ic_launcher_background)
        )
    }
}