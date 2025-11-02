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
            Producto("PO003", "Plátano Maduro", "Racimo de 6 unidades, ideal para batidos y postres.", 1800.0, 80, "Fruta fresca", R.drawable.ic_launcher_background),
            Producto("PO006", "Fresas de Huerta", "Caja de 250g de fresas recién recolectadas.", 5500.0, 95, "Fruta fresca", R.drawable.ic_launcher_background),
            Producto("PO001", "Miel Orgánica", "Frasco de 500g de apicultores locales.", 5000.0, 50, "Productos Orgánicos", R.drawable.ic_launcher_background),
            Producto("PO012", "Huevos de Campo Orgánicos", "Caja de 12 huevos de gallinas libres.", 9500.0, 30, "Productos Orgánicos", R.drawable.ic_launcher_background) ,
            Producto("PO013", "Aceite de Oliva Extra Virgen Orgánico", "Botella de 750 ml de primera prensa en frío.", 15000.0, 25, "Productos Orgánicos", R.drawable.ic_launcher_background) ,
            Producto("PO015", "Pan Integral de Masa Madre Orgánico", "Hogaza de 500g con granos enteros.", 7300.0, 35, "Productos Orgánicos", R.drawable.ic_launcher_background) ,
            Producto("PO016", "Mermelada de Mora Orgánica", "Frasco de 300g con 80% de fruta.", 6100.0, 50, "Productos Orgánicos", R.drawable.ic_launcher_background) ,
            Producto("VR001", "Zanahorias Orgánicas", "Cultivadas sin pesticidas en O'Higgins.", 900.0, 100, "Verduras Orgánicas", R.drawable.ic_launcher_background),
            Producto("PO007", "Tomate Chonto Orgánico", "Bolsa de 1 kg de tomates cultivados sin pesticidas.", 4800.0, 75, "Verduras Organicas", R.drawable.ic_launcher_background) ,
            Producto("PO008", "Lechuga Romana Orgánica", "Unidad de lechuga de hojas firmes y frescas.", 2200.0, 110, "Verduras Organicas", R.drawable.ic_launcher_background) ,
            Producto("PO009", "Zanahorias Baby Orgánicas", "Bolsa de 500g, perfectas para *snacks*.", 3100.0, 55, "Verduras Organicas", R.drawable.ic_launcher_background) ,
            Producto("PO010", "Pimentón Rojo Orgánico", "Unidad de pimentón grande", 3900.0, 40, "Verduras Organicas", R.drawable.ic_launcher_background) ,
            Producto("PO011", "Espinaca Fresca Orgánica", "Paquete de 200g de hojas tiernas para ensaladas.", 2900.0, 70, "Verduras Organicas", R.drawable.ic_launcher_background)
        )
    }
}