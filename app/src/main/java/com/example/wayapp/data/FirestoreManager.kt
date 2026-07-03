package com.example.wayapp.data

import android.net.Uri
import android.util.Log
import com.example.wayapp.model.Chat
import com.example.wayapp.model.Message
import com.example.wayapp.model.ObjetoReportado
import com.example.wayapp.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class FirestoreManager {

    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val coleccionObjetos = "ObjetosReportados"
    private val coleccionUsuarios = "Usuarios"
    private val coleccionChats = "chats"

    companion object {
        private const val TAG_CHATS = "FIRESTORE_CHATS"
        private const val TAG_MESSAGES = "FIRESTORE_MESSAGES"
    }

    // ─────────────────────────────────────────────
    // OBJETOS REPORTADOS
    // ─────────────────────────────────────────────

    fun subirImagen(
        imageUri: Uri,
        onResult: (String?) -> Unit
    ) {
        val storageRef = storage.reference
            .child("images/${UUID.randomUUID()}.jpg")

        storageRef.putFile(imageUri)
            .addOnSuccessListener {
                storageRef.downloadUrl
                    .addOnSuccessListener { url ->
                        onResult(url.toString())
                    }
                    .addOnFailureListener { error ->
                        Log.e(
                            "FIRESTORE_IMAGES",
                            "No se pudo obtener la URL de la imagen",
                            error
                        )
                        onResult(null)
                    }
            }
            .addOnFailureListener { error ->
                Log.e(
                    "FIRESTORE_IMAGES",
                    "No se pudo subir la imagen",
                    error
                )
                onResult(null)
            }
    }

    fun agregarObjeto(
        objeto: ObjetoReportado,
        onResult: (Boolean, String?) -> Unit
    ) {
        val referenciaNuevoDocumento = db
            .collection(coleccionObjetos)
            .document()

        val objetoConId = objeto.copy(
            id = referenciaNuevoDocumento.id,
            idUsuarioReporta = auth.currentUser?.uid
                ?: objeto.idUsuarioReporta
        )

        referenciaNuevoDocumento
            .set(objetoConId)
            .addOnSuccessListener {
                onResult(
                    true,
                    "Objeto publicado exitosamente"
                )
            }
            .addOnFailureListener { excepcion ->
                onResult(
                    false,
                    excepcion.message
                        ?: "Error desconocido al publicar"
                )
            }
    }

    fun escucharObjetosEnTiempoReal(
        onObjetosActualizados: (List<ObjetoReportado>) -> Unit
    ) {
        db.collection(coleccionObjetos)
            .addSnapshotListener { snapshot, error ->

                if (error != null) {
                    Log.e(
                        "FIRESTORE_OBJECTS",
                        "Error al escuchar los objetos",
                        error
                    )
                    return@addSnapshotListener
                }

                val listaObjetos = snapshot
                    ?.documents
                    ?.mapNotNull { documento ->
                        documento.toObject(
                            ObjetoReportado::class.java
                        )
                    }
                    ?: emptyList()

                onObjetosActualizados(listaObjetos)
            }
    }

    // ─────────────────────────────────────────────
    // USUARIOS
    // ─────────────────────────────────────────────

    fun guardarUsuario(
        usuario: Usuario,
        onResult: (Boolean, String?) -> Unit
    ) {
        db.collection(coleccionUsuarios)
            .document(usuario.uid)
            .set(usuario)
            .addOnSuccessListener {
                onResult(true, null)
            }
            .addOnFailureListener { excepcion ->
                onResult(
                    false,
                    excepcion.message
                        ?: "Error al guardar el usuario"
                )
            }
    }

    fun obtenerUsuario(
        uid: String,
        onResult: (Usuario?) -> Unit
    ) {
        if (uid.isBlank()) {
            onResult(null)
            return
        }

        db.collection(coleccionUsuarios)
            .document(uid)
            .get()
            .addOnSuccessListener { documento ->
                onResult(
                    documento.toObject(
                        Usuario::class.java
                    )
                )
            }
            .addOnFailureListener { error ->
                Log.e(
                    "FIRESTORE_USERS",
                    "No se pudo obtener el usuario $uid",
                    error
                )
                onResult(null)
            }
    }

    fun actualizarUsuario(
        uid: String,
        nombre: String,
        role: String,
        major: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        val datos = mapOf(
            "nombre" to nombre,
            "role" to role,
            "major" to major
        )

        db.collection(coleccionUsuarios)
            .document(uid)
            .set(datos, SetOptions.merge())
            .addOnSuccessListener {
                onResult(true, null)
            }
            .addOnFailureListener { excepcion ->
                onResult(
                    false,
                    excepcion.message
                        ?: "Error al actualizar el usuario"
                )
            }
    }

    // ─────────────────────────────────────────────
    // CHAT
    // ─────────────────────────────────────────────

    /**
     * Genera el mismo identificador para una conversación,
     * independientemente de quién envíe el primer mensaje.
     */
    fun getChatId(otherUserId: String): String {
        val myId = auth.currentUser?.uid.orEmpty()

        if (myId.isBlank() || otherUserId.isBlank()) {
            return ""
        }

        return crearChatId(
            firstUserId = myId,
            secondUserId = otherUserId
        )
    }

    private fun crearChatId(
        firstUserId: String,
        secondUserId: String
    ): String {
        return if (firstUserId < secondUserId) {
            "${firstUserId}_${secondUserId}"
        } else {
            "${secondUserId}_${firstUserId}"
        }
    }

    /**
     * Guarda el mensaje y los metadatos del chat en una sola
     * operación mediante WriteBatch.
     */
    fun sendMessage(
        otherUserId: String,
        otherUserName: String,
        myName: String,
        text: String
    ) {
        val myId = auth.currentUser?.uid.orEmpty()
        val cleanText = text.trim()

        if (
            myId.isBlank() ||
            otherUserId.isBlank() ||
            cleanText.isBlank() ||
            otherUserId == myId
        ) {
            return
        }

        val chatId = crearChatId(
            firstUserId = myId,
            secondUserId = otherUserId
        )

        val timestamp = System.currentTimeMillis()

        val chatReference = db
            .collection(coleccionChats)
            .document(chatId)

        val messageReference = chatReference
            .collection("messages")
            .document()

        val messageData = hashMapOf<String, Any>(
            "senderId" to myId,
            "text" to cleanText,
            "timestamp" to timestamp
        )

        val chatData = hashMapOf<String, Any>(
            "participants" to listOf(
                myId,
                otherUserId
            ),
            "lastMessage" to cleanText,
            "lastTimestamp" to timestamp,

            // Datos desde mi perspectiva
            "otherUserId_$myId" to otherUserId,
            "otherUserName_$myId" to
                    otherUserName.ifBlank { "Usuario" },

            // Datos desde la perspectiva del otro usuario
            "otherUserId_$otherUserId" to myId,
            "otherUserName_$otherUserId" to
                    myName.ifBlank { "Usuario" }
        )

        val batch = db.batch()

        batch.set(
            messageReference,
            messageData
        )

        batch.set(
            chatReference,
            chatData,
            SetOptions.merge()
        )

        batch.commit()
            .addOnSuccessListener {
                Log.d(
                    TAG_MESSAGES,
                    "Mensaje enviado correctamente"
                )
            }
            .addOnFailureListener { error ->
                Log.e(
                    TAG_MESSAGES,
                    "No se pudo enviar el mensaje",
                    error
                )
            }
    }

    /**
     * Escucha los mensajes de una conversación.
     */
    fun listenMessages(
        chatId: String,
        onChange: (List<Message>) -> Unit,
        onError: (Exception) -> Unit = {}
    ): ListenerRegistration {

        if (chatId.isBlank()) {
            onChange(emptyList())

            return object : ListenerRegistration {
                override fun remove() = Unit
            }
        }

        return db.collection(coleccionChats)
            .document(chatId)
            .collection("messages")
            .orderBy(
                "timestamp",
                Query.Direction.ASCENDING
            )
            .addSnapshotListener { snapshot, error ->

                if (error != null) {
                    Log.e(
                        TAG_MESSAGES,
                        "Error al escuchar los mensajes",
                        error
                    )

                    onError(error)
                    return@addSnapshotListener
                }

                val messages = snapshot
                    ?.documents
                    ?.map { documento ->
                        Message(
                            id = documento.id,
                            senderId = documento
                                .getString("senderId")
                                .orEmpty(),
                            text = documento
                                .getString("text")
                                .orEmpty(),
                            timestamp = documento
                                .getLong("timestamp")
                                ?: 0L
                        )
                    }
                    ?: emptyList()

                onChange(messages)
            }
    }

    /**
     * Escucha todos los chats donde participa el usuario.
     *
     * No utiliza orderBy en Firestore para evitar exigir
     * un índice compuesto. El orden se realiza en Kotlin.
     */
    fun listenMyChats(
        onChange: (List<Chat>) -> Unit,
        onError: (Exception) -> Unit = {}
    ): ListenerRegistration {

        val myId = auth.currentUser?.uid.orEmpty()

        if (myId.isBlank()) {
            onChange(emptyList())

            return object : ListenerRegistration {
                override fun remove() = Unit
            }
        }

        return db.collection(coleccionChats)
            .whereArrayContains(
                "participants",
                myId
            )
            .addSnapshotListener { snapshot, error ->

                if (error != null) {
                    Log.e(
                        TAG_CHATS,
                        "Error al escuchar las conversaciones",
                        error
                    )

                    /*
                     * No enviamos emptyList() aquí, porque eso
                     * borraría los chats que ya estaban visibles.
                     */
                    onError(error)
                    return@addSnapshotListener
                }

                val chats = snapshot
                    ?.documents
                    ?.mapNotNull { documento ->

                        /*
                         * Primero busca el campo guardado para
                         * este usuario. Si es un chat antiguo,
                         * obtiene el otro UID desde participants.
                         */
                        val otherUserId =
                            documento.getString(
                                "otherUserId_$myId"
                            )
                                ?: documento
                                    .get("participants")
                                    .let { participants ->
                                        (participants as? List<*>)
                                            ?.filterIsInstance<String>()
                                            ?.firstOrNull {
                                                it != myId
                                            }
                                    }
                                ?: return@mapNotNull null

                        Chat(
                            chatId = documento.id,
                            otherUserId = otherUserId,
                            otherUserName = documento
                                .getString(
                                    "otherUserName_$myId"
                                )
                                ?.ifBlank { "Usuario" }
                                ?: "Usuario",
                            lastMessage = documento
                                .getString("lastMessage")
                                .orEmpty(),
                            lastTimestamp = documento
                                .getLong("lastTimestamp")
                                ?: 0L
                        )
                    }
                    ?.sortedByDescending {
                        it.lastTimestamp
                    }
                    ?: emptyList()

                Log.d(
                    TAG_CHATS,
                    "Conversaciones encontradas: ${chats.size}"
                )

                onChange(chats)
            }
    }
}