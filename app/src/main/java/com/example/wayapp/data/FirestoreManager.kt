package com.example.wayapp.data

import android.net.Uri
import com.example.wayapp.model.ObjetoReportado
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class FirestoreManager {

    // Instancias de Firestore y Storage
    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    private val coleccionObjetos = "ObjetosReportados"

    // Sube una imagen a Firestore Storage y lo devuelve como URL
    fun subirImagen(imageUri: Uri, onResult: (String?) -> Unit) {
        // Se genera un nombre unico para la foto
        val storageRef = storage.reference.child("images/${UUID.randomUUID()}.jpg")

        // Se sube el archivo
        storageRef.putFile(imageUri)
            .addOnSuccessListener {
                // Si todo sale bien solicitamos la URL para guardarla en Firestore
                storageRef.downloadUrl.addOnSuccessListener { url ->
                    onResult(url.toString()) // Devolvemos la URL
                }
            }
            .addOnFailureListener {
                onResult(null) // Error al subir
            }
    }

    // Publica un objeto con el URL de la img
    fun agregarObjeto(objeto: ObjetoReportado, onResult: (Boolean, String?) -> Unit) {
        val referenciaNuevoDocumento = db.collection(coleccionObjetos).document()
        val objetoConId = objeto.copy(id = referenciaNuevoDocumento.id)

        referenciaNuevoDocumento.set(objetoConId)
            .addOnSuccessListener {
                onResult(true, "Objeto publicado exitosamente")
            }
            .addOnFailureListener { excepcion ->
                onResult(false, excepcion.message ?: "Error desconocido al publicar")
            }
    }

    // Funcion que escucha a los objetos que se subiran a Firestore y al ROOM
    fun escucharObjetosEnTiempoReal(onObjetosActualizados: (List<ObjetoReportado>) -> Unit) {
        db.collection(coleccionObjetos).addSnapshotListener { snapshot, error ->
            if (error != null) return@addSnapshotListener
            if (snapshot != null) {
                val listaObjetos = mutableListOf<ObjetoReportado>()
                for (documento in snapshot.documents) {
                    val objeto = documento.toObject(ObjetoReportado::class.java)
                    if (objeto != null) listaObjetos.add(objeto)
                }
                onObjetosActualizados(listaObjetos)
            }
        }
    }
}