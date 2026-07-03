package com.example.wayapp.model

data class Usuario(
    val uid: String = "",
    val nombre: String = "",
    val correo: String = "",
    val role: String = "Estudiante",
    val major: String = ""
)