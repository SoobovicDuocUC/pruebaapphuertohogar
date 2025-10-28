package com.example.projectohuertoapp.model

import androidx.annotation.DrawableRes

data class Producto(
    val id: String,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val stock: Int,
    val categoria: String,
    @DrawableRes val imagenResId: Int // <-- CAMBIO IMPORTANTE
)