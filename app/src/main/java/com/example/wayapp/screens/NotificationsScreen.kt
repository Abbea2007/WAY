package com.example.wayapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wayapp.ui.theme.*

data class NotificationItem(
    val title: String,
    val description: String,
    val time: String,
    val icon: ImageVector,
    val iconColor: Color,
    val iconBackground: Color,
    val isRead: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    onBack: () -> Unit = {}
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val isDarkMode = MaterialTheme.colorScheme.background == WayDarkBackground

    val notifications = listOf(
        NotificationItem(
            "¡Coincidencia encontrada!",
            "Tu publicación \"Mochila negra\" tiene una posible coincidencia.",
            "Hace 5 min",
            Icons.Outlined.NotificationsActive,
            WayGreen,
            WayGreenSoft,
            false
        ),
        NotificationItem(
            "Nuevo mensaje",
            "Juan te envió un mensaje sobre \"Termo azul marino\".",
            "Hace 1 h",
            Icons.AutoMirrored.Outlined.Chat,
            WayPurple,
            WayPurpleSoft,
            false
        ),
        NotificationItem(
            "Actualización",
            "Tu publicación \"Paraguas negro\" fue marcada como finalizada.",
            "Ayer",
            Icons.Outlined.CheckCircle,
            Color(0xFF3B82F6),
            Color(0xFFEFF6FF),
            true
        ),
        NotificationItem(
            "Recordatorio",
            "Tu publicación \"Libreta con espiral\" lleva 7 días activa.",
            "2 may",
            Icons.Outlined.Notifications,
            Color(0xFFF59E0B),
            Color(0xFFFFFBEB),
            true
        ),
        NotificationItem(
            "¡Encontrado!",
            "Alguien ha reportado un objeto similar a tus \"Llaves de carro\".",
            "3 may",
            Icons.Outlined.NotificationsActive,
            WayGreen,
            WayGreenSoft,
            true
        )
    )

    val filteredNotifications = if (selectedTab == 0) {
        notifications
    } else {
        notifications.filter { !it.isRead }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Notificaciones",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = "Atrás",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* Ajustes de notificaciones */ }) {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = "Configuración",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp)
            ) {
                NotificationTabItem(
                    text = "Todas",
                    selected = selectedTab == 0,
                    modifier = Modifier.weight(1f),
                    onClick = { selectedTab = 0 }
                )
                NotificationTabItem(
                    text = "No leídas",
                    selected = selectedTab == 1,
                    modifier = Modifier.weight(1f),
                    onClick = { selectedTab = 1 }
                )
            }

            HorizontalDivider(color = WayBorder.copy(alpha = 0.5f))

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredNotifications) { notification ->
                    NotificationRow(notification, isDarkMode)
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 24.dp),
                        color = WayBorder.copy(alpha = 0.3f)
                    )
                }
            }
        }
    }
}

@Composable
fun NotificationTabItem(
    text: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = text,
            fontSize = 15.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            color = if (selected) WayPurple else WayTextMuted,
            modifier = Modifier.padding(vertical = 12.dp)
        )
        if (selected) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(3.dp)
                    .clip(CircleShape)
                    .background(WayPurple)
            )
        } else {
            Spacer(modifier = Modifier.height(3.dp))
        }
    }
}

@Composable
fun NotificationRow(notification: NotificationItem, isDarkMode: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (!notification.isRead && !isDarkMode) WayPurple.copy(alpha = 0.03f) else Color.Transparent)
            .clickable { /* Ver detalle */ }
            .padding(horizontal = 24.dp, vertical = 20.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(if (isDarkMode) notification.iconColor.copy(alpha = 0.15f) else notification.iconBackground),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = notification.icon,
                contentDescription = null,
                tint = notification.iconColor,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = notification.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = notification.time,
                    fontSize = 11.sp,
                    color = WayTextMuted
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = notification.description,
                fontSize = 13.sp,
                color = WayTextSecondary,
                lineHeight = 18.sp
            )
        }
    }
}
