package com.example.kioscopueblito

data class Producto(
    val nombre: String,
    val precio: Double,
    val imagen: Int,
    var cantidad: Int = 1
)
