package com.example.projectohuertoapp.model

data class Producto(
    val id: Long,
    val codigo: String,
    val nombre: String,
    val precio: Int,
    val img: String,
    val categoria: String,
    val precioKilo: String?,
    val descripcion: String?
)