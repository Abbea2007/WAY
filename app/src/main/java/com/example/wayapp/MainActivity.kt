package com.example.wayapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import com.example.wayapp.ui.navigation.AppNavigation
import com.example.wayapp.ui.theme.ThemeMode
import com.example.wayapp.ui.theme.WAYAPPTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            var themeMode by remember { mutableStateOf(ThemeMode.System) }

            WAYAPPTheme(themeMode = themeMode) {
                AppNavigation(
                    themeMode = themeMode,
                    onThemeChange = { themeMode = it }
                )
            }
        }
    }
}