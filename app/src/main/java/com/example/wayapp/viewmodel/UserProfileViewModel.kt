package com.example.wayapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.wayapp.R
import com.example.wayapp.data.FirestoreManager
import com.google.firebase.auth.FirebaseAuth

class UserProfileViewModel : ViewModel() {

    private val firestoreManager = FirestoreManager()
    private val auth = FirebaseAuth.getInstance()

    var name by mutableStateOf("Username")
    var role by mutableStateOf("Estudiante")
    var major by mutableStateOf("Ingeniería de Software")
    var profilePhotoRes by mutableIntStateOf(R.drawable.img)
    val studentId = "ID: 20231345"

    // Guarda el uid del usuario que está cargado actualmente en el ViewModel.
    // Sirve para detectar si cambió de cuenta y evitar mostrar datos mezclados.
    private var uidCargado: String? = null

    init {
        cargarUsuario()
    }

    /**
     * Carga el nombre, rol y carrera reales desde Firestore usando el uid
     * del usuario actualmente logueado.
     *
     * IMPORTANTE: llama a esta función manualmente justo después de un login
     * exitoso (en AuthScreen o donde manejes el resultado de iniciarSesion()).
     * El init{} de este ViewModel solo se ejecuta la primera vez que se crea
     * la instancia; si el mismo ViewModel sobrevive entre sesiones (por ejemplo
     * porque sigue vivo en el NavBackStackEntry), necesitas forzar la recarga.
     */
    fun cargarUsuario() {
        val uid = auth.currentUser?.uid

        if (uid == null) {
            limpiarUsuario()
            return
        }

        // Si ya tenemos cargado a este mismo usuario, no hace falta pedirlo de nuevo
        if (uid == uidCargado) return

        firestoreManager.obtenerUsuario(uid) { usuario ->
            // Verificamos que el usuario logueado siga siendo el mismo antes de aplicar
            // los datos (evita condiciones de carrera si el usuario cambió de cuenta rápido)
            if (auth.currentUser?.uid != uid) return@obtenerUsuario

            if (usuario != null) {
                name = usuario.nombre.ifBlank { "Username" }
                role = usuario.role.ifBlank { "Estudiante" }
                major = usuario.major.ifBlank { "" }
            } else {
                // No existe documento para este usuario en Firestore todavía
                name = "Username"
                role = "Estudiante"
                major = ""
            }
            uidCargado = uid
        }
    }

    /**
     * Resetea el estado a los valores por defecto.
     * Llama a esta función cuando el usuario cierra sesión, para que la
     * próxima cuenta que inicie sesión no vea datos de la cuenta anterior
     * mientras se termina de cargar la nueva.
     */
    fun limpiarUsuario() {
        name = "Username"
        role = "Estudiante"
        major = "Ingeniería de Software"
        uidCargado = null
    }

    /**
     * Actualiza el estado local inmediatamente (para que la UI responda al toque)
     * y además persiste los cambios en Firestore para que sobrevivan a cerrar la app.
     */
    fun updateProfile(newName: String, newRole: String, newMajor: String) {
        name = newName
        role = newRole
        major = newMajor

        val uid = auth.currentUser?.uid ?: return
        firestoreManager.actualizarUsuario(uid, newName, newRole, newMajor) { _, _ ->
            // Si falla, el nombre ya quedó actualizado en pantalla;
            // se puede reintentar la próxima vez que se guarde el perfil.
        }
    }
}