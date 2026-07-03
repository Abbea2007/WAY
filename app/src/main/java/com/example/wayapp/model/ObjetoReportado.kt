package com.example.wayapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// 1. Le decimos a Room que esta clase será una tabla en la base de datos
@Entity(tableName = "objetos_reportados")
data class ObjetoReportado(
    @PrimaryKey
    val id: String = "",
    val nombre: String = "",
    val categoria: String = "",
    val ubicacion: String = "",
    val fechaHora: String = "",
    val descripcion: String = "",
    val estado: String = "",
    val imageUrl: String = "",
    val idUsuarioReporta: String = "usuario_demo_123"
)