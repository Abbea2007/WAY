package com.example.wayapp.data

import com.example.wayapp.model.ObjetoReportado
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreManager {

    // 1. Encapsulamiento: Instancia privada de la base de datos
    private val db = FirebaseFirestore.getInstance()

    // Nombre de la colección (como la carpeta principal donde irán todos los objetos)
    private val coleccion = "ObjetosReportados"

    /**
     * Función para subir un nuevo objeto perdido/encontrado a la nube
     */
    fun agregarObjeto(objeto: ObjetoReportado, onResult: (Boolean, String?) -> Unit) {

        // 2. Le pedimos a Firebase que nos genere un ID único (ej. "aB3x9kL...")
        val referenciaNuevoDocumento = db.collection(coleccion).document()

        // 3. Le inyectamos ese ID a nuestro objeto usando el método copy() de las data classes
        val objetoConId = objeto.copy(id = referenciaNuevoDocumento.id)

        // 4. Subimos el objeto a Firestore
        referenciaNuevoDocumento.set(objetoConId)
            .addOnSuccessListener {
                // Si todo sale bien
                onResult(true, "Objeto publicado exitosamente")
            }
            .addOnFailureListener { excepcion ->
                // Si ocurre un error (ej. se fue el internet)
                onResult(false, excepcion.message ?: "Error desconocido al publicar")
            }
    }
}