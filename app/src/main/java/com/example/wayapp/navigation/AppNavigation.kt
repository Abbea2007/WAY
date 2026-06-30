package com.example.wayapp.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.wayapp.screens.*
import com.example.wayapp.ui.theme.ThemeMode
import com.example.wayapp.viewmodel.UserProfileViewModel

// ENRUTADOR PRINCIPAL: Centraliza la navegación y el flujo de pantallas de la aplicación.
@Composable
fun AppNavigation(
    themeMode: ThemeMode,
    onThemeChange: (ThemeMode) -> Unit
) {
    val navController = rememberNavController()
    val userProfileViewModel: UserProfileViewModel = viewModel()

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
                onBack = { navController.popBackStack() }
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
                onNotificationsClick = {
                    navController.navigate("notifications")
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
                onChatClick = { chatId ->
                    navController.navigate("chat_detail/$chatId")
                }
            )
        }

        composable("chat_detail/{chatId}") { backStackEntry ->
            val chatId = backStackEntry.arguments?.getString("chatId") ?: ""
            ChatDetailScreen(
                chatId = chatId,
                onBack = {
                    navController.popBackStack()
                }
            )
        }
        composable("notifications") {
            NotificationsScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
