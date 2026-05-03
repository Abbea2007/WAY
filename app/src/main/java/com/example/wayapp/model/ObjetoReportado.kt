package com.example.wayapp.model

// Usamos una Data Class porque su único propósito es almacenar estado/datos
data class ObjetoReportado(
    val id: String = "", // ID único que generará Firebase
    val titulo: String = "", // Ej: "Audífonos Inalámbricos"
    val descripcion: String = "", // Ej: "Se encontraron en la sala de lectura..."
    val estado: String = "", // "PERDIDO" o "ENCONTRADO"
    val ubicacionGeneral: String = "", // Ej: "Biblioteca Central"
    val fechaHora: String = "", // Ej: "Hoy, 10:30 a.m."
    val idUsuarioReporta: String = "", // El ID del usuario que creó este reporte

    // Campos opcionales (por eso tienen valor por defecto vacío)
    val marca: String = "",
    val color: String = "",
    val condicion: String = "",

    // Aquí luego agregaremos la URL de la foto cuando configuremos Firebase Storage
    val imageUrl: String = ""
)