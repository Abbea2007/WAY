package com.example.wayapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.wayapp.ui.screens.OnboardingScreen
import com.example.wayapp.ui.theme.WAYAPPTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            WAYAPPTheme {
                OnboardingScreen()
            }
        }
    }
}