package com.example.projectohuertoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectohuertoapp.R
import com.example.projectohuertoapp.model.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class CatalogoViewModel : ViewModel() {

    private val _productosOriginal = MutableStateFlow<List<Producto>>(emptyList())

    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText.asStateFlow()

    val productosFiltrados: StateFlow<List<Producto>> = _productosOriginal
        .combine(_searchText) { productos, text ->
            if (text.isBlank()) {
                productos // Si el texto está vacío, retorna la lista completa
            } else {
                val lowerCaseText = text.trim().lowercase()
                productos.filter { producto ->
                    // busca coincidencias en nombre, descripción o categoría
                    producto.nombre.lowercase().contains(lowerCaseText) ||
                            producto.descripcion.lowercase().contains(lowerCaseText) ||
                            producto.categoria.lowercase().contains(lowerCaseText)
                }
            }
        }.stateIn( // Convierte el Flow combinado en un StateFlow
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000), // Comienza a escuchar cuando la UI está activa
            initialValue = _productosOriginal.value
        )

    init {
        cargarProductosDeEjemplo()
    }

    // Función para actualizar el texto de búsqueda (llamada desde la UI)
    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    private fun cargarProductosDeEjemplo() {
        // Carga la lista completa de productos en la lista original
        _productosOriginal.value = listOf(
            Producto("FR001", "Manzanas Fuji", "Crujientes y dulces, del Valle del Maule.", 1200.0, 150, "Frutas Frescas", R.drawable.fuji_apples),
            Producto("PO003", "Plátano Maduro", "Racimo de 6 unidades, ideal para batidos y postres.", 1800.0, 80, "Fruta fresca", R.drawable.platano_maduro),
            Producto("PO006", "Fresas de Huerta", "Caja de 250g de fresas recién recolectadas.", 5500.0, 95, "Fruta fresca", R.drawable.fresas_huerta),
            Producto("PO001", "Miel Orgánica", "Frasco de 500g de apicultores locales.", 5000.0, 50, "Productos Orgánicos", R.drawable.miel),
            Producto("PO012", "Huevos de Campo Orgánicos", "Caja de 12 huevos de gallinas libres.", 9500.0, 30, "Productos Orgánicos", R.drawable.huevo_organicos) ,
            Producto("PO013", "Aceite de Oliva Extra Virgen Orgánico", "Botella de 750 ml de primera prensa en frío.", 15000.0, 25, "Productos Orgánicos", R.drawable.aceite_oliva) ,
            Producto("PO016", "Mermelada de Mora Orgánica", "Frasco de 300g con 80% de fruta.", 6100.0, 50, "Productos Orgánicos", R.drawable.merme_mora) ,
            Producto("VR001", "Zanahorias Orgánicas", "Cultivadas sin pesticidas en O'Higgins.", 900.0, 100, "Verduras Orgánicas", R.drawable.zanahoria_organica),
            Producto("PO007", "Tomate Chonto Orgánico", "Bolsa de 1 kg de tomates cultivados sin pesticidas.", 4800.0, 75, "Verduras Organicas", R.drawable.tomate_chonto) ,
            Producto("PO008", "Lechuga Romana Orgánica", "Unidad de lechuga de hojas firmes y frescas.", 2200.0, 110, "Verduras Organicas", R.drawable.lechuga_organica) ,
            Producto("PO009", "Zanahorias Baby Orgánicas", "Bolsa de 500g, perfectas para *snacks*.", 3100.0, 55, "Verduras Organicas", R.drawable.baby_carrots) ,
            Producto("PO010", "Pimentón Rojo Orgánico", "Unidad de pimentón grande", 3900.0, 40, "Verduras Organicas", R.drawable.red_pepper) ,
            Producto("PO011", "Espinaca Fresca Orgánica", "Paquete de 200g de hojas tiernas para ensaladas.", 2900.0, 70, "Verduras Organicas", R.drawable.espinaca_organica)
        )
    }
}