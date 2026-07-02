package com.example.wayapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.wayapp.auth.AuthManager
import com.example.wayapp.screens.*
import com.example.wayapp.ui.theme.ThemeMode
import com.example.wayapp.viewmodel.UserProfileViewModel
import java.net.URLDecoder
import java.net.URLEncoder

// ENRUTADOR PRINCIPAL: Centraliza la navegación y el flujo de pantallas de la aplicación.
@Composable
fun AppNavigation(
    themeMode: ThemeMode,
    onThemeChange: (ThemeMode) -> Unit
) {
    val navController = rememberNavController()
    val userProfileViewModel: UserProfileViewModel = viewModel()
    val authManager = remember { AuthManager() }

    NavHost(
        navController = navController,
        startDestination = "onboarding"
    ) {
        composable("onboarding") {
            OnboardingScreen(
                onFinish = {
                    navController.navigate("auth") {
                        popUpTo("onboarding") { inclusive = true }
                    }
                }
            )
        }

        composable("auth") {
            AuthScreen(
                onLoginSuccess = {
                    // Forzamos la recarga de los datos del usuario que acaba de iniciar sesión
                    userProfileViewModel.cargarUsuario()

                    navController.navigate("home") {
                        popUpTo("auth") { inclusive = true }
                    }
                }
            )
        }

        composable("home") {
            HomeScreen(
                themeMode = themeMode,
                onThemeChange = onThemeChange,
                onProfileClick = {
                    navController.navigate("profile")
                },
                onFilterClick = {
                    navController.navigate("filters")
                },
                onItemClick = { itemId ->
                    navController.navigate("item_detail/$itemId")
                },
                userViewModel = userProfileViewModel
            )
        }

        composable("item_detail/{itemId}") { backStackEntry ->
            val itemId = backStackEntry.arguments?.getString("itemId") ?: ""
            ItemDetailScreen(
                itemId = itemId,
                onBack = { navController.popBackStack() },
                onContactClick = { otherUserId, otherUserName ->
                    val nombreCodificado = URLEncoder.encode(otherUserName, "UTF-8")
                    navController.navigate("chat_detail/$otherUserId/$nombreCodificado")
                }
            )
        }

        composable("filters") {
            FilterScreen(
                onBack = { navController.popBackStack() },
                onApply = { navController.popBackStack() }
            )
        }

        composable("profile") {
            ProfileScreen(
                onBack = {
                    navController.popBackStack()
                },
                onMyPublicationsClick = {
                    navController.navigate("my_publications")
                },
                onMyMessagesClick = {
                    navController.navigate("my_messages")
                },
                onLogoutClick = {
                    authManager.cerrarSesion()
                    userProfileViewModel.limpiarUsuario()
                    navController.navigate("auth") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                userViewModel = userProfileViewModel
            )
        }

        composable("my_publications") {
            MyPublicationsScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable("my_messages") {
            MyMessagesScreen(
                onBack = {
                    navController.popBackStack()
                },
                onChatClick = { otherUserId, otherUserName ->
                    val nombreCodificado = URLEncoder.encode(otherUserName, "UTF-8")
                    navController.navigate("chat_detail/$otherUserId/$nombreCodificado")
                }
            )
        }

        composable(
            route = "chat_detail/{otherUserId}/{otherUserName}",
            arguments = listOf(
                navArgument("otherUserId") { type = NavType.StringType },
                navArgument("otherUserName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val otherUserId = backStackEntry.arguments?.getString("otherUserId") ?: ""
            val otherUserNameCodificado = backStackEntry.arguments?.getString("otherUserName") ?: ""
            val otherUserName = URLDecoder.decode(otherUserNameCodificado, "UTF-8")

            ChatDetailScreen(
                otherUserId = otherUserId,
                otherUserName = otherUserName,
                onBack = {
                    navController.popBackStack()
                },
                userViewModel = userProfileViewModel
            )
        }
    }
}