package com.example.wayapp.model

data class ObjetoReportado(
    val id: String = "",
    val nombre: String = "",          // [NO OPCIONAL en UI] Ej: "Botella de agua" (Reemplaza a 'titulo')
    val categoria: String = "",       // [NO OPCIONAL en UI] Sugerido para poder filtrar: "Ropa", "Electrónica", etc.
    val ubicacion: String = "",       // [NO OPCIONAL en UI] Dónde se perdió o encontró
    val fechaHora: String = "",       // [NO OPCIONAL en UI]
    val descripcion: String = "",     // [OPCIONAL en UI]
    val imageUrl: String = "",        // [OPCIONAL en UI] URL de la foto cuando configures Storage
    val estado: String = "PERDIDO",   // "PERDIDO" o "ENCONTRADO"
    val idUsuarioReporta: String = "" // El ID de quien lo sube
)