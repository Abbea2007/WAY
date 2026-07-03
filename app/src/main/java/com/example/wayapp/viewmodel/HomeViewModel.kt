package com.example.wayapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.wayapp.data.FirestoreManager
import com.example.wayapp.data.WayDatabase
import com.example.wayapp.model.ObjetoReportado
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.UUID

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    // 1. Se instancia ROOM y Firebase
    private val dao = WayDatabase.getDatabase(application).objetoDao()
    private val firestoreManager = FirestoreManager()

    // 2. Variable que hara cambios en tiempo real hacia la pantalla
    val objetosLocales: Flow<List<ObjetoReportado>> = dao.obtenerTodosLosObjetos()

    init {
        // 3. Cuando la app abre automaticamente se sincronizan los datos
        sincronizarFirebaseConRoom()
    }

    private fun sincronizarFirebaseConRoom() {
        // Escuchamos la nube 24/7
        firestoreManager.escucharObjetosEnTiempoReal { listaDesdeLaNube ->
            // Cuando Firebase detecta un cambio (alguien subio o borro algo),
            // se recibe la lista nueva y se guarda de golpe en ROOM
            viewModelScope.launch {
                dao.insertarObjetos(listaDesdeLaNube)
            }
        }
    }

    // btn de prueba para probar ROOM
    fun guardarDatoDePrueba() {
        viewModelScope.launch {
            val objetoPrueba = ObjetoReportado(
                id = UUID.randomUUID().toString(),
                nombre = "Mochila HP (Prueba Room)",
                categoria = "Accesorios",
                ubicacion = "Cafetería Central",
                fechaHora = "Hoy en la tarde",
                descripcion = "Dato inyectado de prueba",
                estado = "PERDIDO"
            )
            // Ojo: Esto solo lo guarda localmente. Luego haremos la función para subirlo a la nube.
            dao.insertarObjeto(objetoPrueba)
        }
    }

    // Función para buscar un objeto específico por su ID
    fun obtenerObjetoPorId(id: String): Flow<ObjetoReportado?> {
        return dao.obtenerObjetoPorId(id)
    }
}