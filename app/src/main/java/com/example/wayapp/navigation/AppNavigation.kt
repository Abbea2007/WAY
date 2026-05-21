package com.example.wayapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.wayapp.screens.AuthScreen
import com.example.wayapp.screens.HomeScreen
import com.example.wayapp.screens.OnboardingScreen
import com.example.wayapp.ui.theme.ThemeMode
// ENRUTADOR PRINCIPAL: Centraliza la navegación y el flujo de pantallas de la aplicación.
@Composable
fun AppNavigation(
    themeMode: ThemeMode,
    onThemeChange: (ThemeMode) -> Unit
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "onboarding"
    ) {
        composable("onboarding") {
            OnboardingScreen(
                onFinish = {
                    navController.navigate("auth") {
                        // Limpieza de pila: Elimina "onboarding" del historial (inclusive = true).
                        // Si el usuario está en "auth" y presiona el botón "Atrás",
                        // saldrá de la app en lugar de volver a ver la bienvenida.
                        popUpTo("onboarding") { inclusive = true }
                    }
                }
            )
        }

        composable("auth") {
            AuthScreen(
                onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("auth") { inclusive = true }
                    }
                }
            )
        }

        composable("home") {
            HomeScreen(
                themeMode = themeMode,
                onThemeChange = onThemeChange
            )
        }
    }
}