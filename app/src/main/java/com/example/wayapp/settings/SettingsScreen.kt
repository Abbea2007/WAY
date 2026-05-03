package com.example.wayapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.PhoneAndroid
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wayapp.ui.theme.*

enum class ThemeOption {
    Light, Dark, System
}

@Composable
fun SettingsScreen(
    selectedTheme: ThemeMode,
    onThemeChange: (ThemeMode) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = "Configuración",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Apariencia",
            color = if (selectedTheme == ThemeMode.Dark) WayDarkTextSecondary else WayTextSecondary,
            fontSize = 14.sp
        )

        SettingsOptionCard(
            title = "Modo claro",
            icon = Icons.Outlined.LightMode,
            selected = selectedTheme == ThemeMode.Light,
            onClick = { onThemeChange(ThemeMode.Light) }
        )

        SettingsOptionCard(
            title = "Modo oscuro",
            icon = Icons.Outlined.DarkMode,
            selected = selectedTheme == ThemeMode.Dark,
            onClick = { onThemeChange(ThemeMode.Dark) }
        )

        SettingsOptionCard(
            title = "Según el sistema",
            icon = Icons.Outlined.PhoneAndroid,
            selected = selectedTheme == ThemeMode.System,
            onClick = { onThemeChange(ThemeMode.System) }
        )
    }
}

@Composable
fun SettingsOptionCard(
    title: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable { onClick() }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (selected) WayPurple else WayTextMuted,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(14.dp))

        Text(
            text = title,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 15.sp,
            modifier = Modifier.weight(1f)
        )

        RadioButton(
            selected = selected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = WayPurple,
                unselectedColor = WayTextMuted
            )
        )
    }
}