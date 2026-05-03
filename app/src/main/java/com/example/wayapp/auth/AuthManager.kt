package com.example.wayapp.auth

import com.google.firebase.auth.FirebaseAuth

class AuthManager {

    // Instancia de Firebase Auth
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    /**
     * Función para registrar un nuevo usuario en la app WAY
     */
    fun registrarUsuario(correo: String, contrasena: String, onResult: (Boolean, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(correo, contrasena)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // El registro fue exitoso
                    onResult(true, "Usuario registrado correctamente")
                } else {
                    // Hubo un error (ej. el correo ya existe, contraseña muy corta)
                    onResult(false, task.exception?.message)
                }
            }
    }

    /**
     * Función para iniciar sesión con un usuario existente
     */
    fun iniciarSesion(correo: String, contrasena: String, onResult: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(correo, contrasena)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Login exitoso
                    onResult(true, "Inicio de sesión exitoso")
                } else {
                    // Error (ej. contraseña incorrecta)
                    onResult(false, "Credenciales incorrectas")
                }
            }
    }
}