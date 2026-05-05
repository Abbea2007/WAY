package com.example.wayapp.auth

import com.google.firebase.auth.FirebaseAuth

class AuthManager {

    // Instancia de Firebase Auth
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    /**
     * Función para registrar un nuevo usuario en la app WAY
     */
    fun registrarUsuario(fullName: String, correo: String, contrasena: String, onResult: (Boolean, String?) -> Unit) {

        // 1. Validar nombre de usuario no sea Blank
        if (fullName.isBlank()) {
            onResult(false, "Por favor coloque su nombre de usuario")
            return
        }

        // 2. Validar dominio del correo
        if (!correo.endsWith("@uamv.edu.ni") && !correo.endsWith("@gmail.com")) {
            onResult(false, "Solo se permiten correos @uamv.edu.ni o @gmail.com")
            return
        }



        // 3. Registrar en Firebase
        auth.createUserWithEmailAndPassword(correo, contrasena)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    // 3. Enviar correo de verificación
                    auth.currentUser?.sendEmailVerification()
                        ?.addOnCompleteListener { emailTask ->
                            if (emailTask.isSuccessful) {
                                // Cerramos la sesión recién creada para obligarlo a verificar antes de entrar
                                auth.signOut()
                                onResult(true, "Registro exitoso. Revisa tu bandeja de entrada para verificar tu cuenta.")
                            } else {
                                auth.signOut()
                                onResult(false, "Registro exitoso, pero ocurrió un error al enviar el correo de verificación.")
                            }
                        }

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

                    // Validamos que el correo haya sido verificado
                    val user = auth.currentUser
                    if (user != null && user.isEmailVerified) {
                        // Login y verificación exitosos
                        onResult(true, "Inicio de sesión exitoso")
                    } else {
                        // El correo no está verificado, cerramos sesión
                        auth.signOut()
                        onResult(false, "Por favor, verifica tu correo electrónico antes de iniciar sesión.")
                    }

                } else {
                    // Error (ej. contraseña incorrecta o cuenta no existe)
                    onResult(false, "Credenciales incorrectas")
                }
            }
    }
}