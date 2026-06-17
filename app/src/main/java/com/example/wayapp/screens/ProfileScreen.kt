package com.example.wayapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wayapp.R
import com.example.wayapp.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onBack: () -> Unit = {},
    onMyPublicationsClick: () -> Unit = {},
    onMyMessagesClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {}
) {
    var isEditing by remember { mutableStateOf(false) }

    // User data state
    var name by remember { mutableStateOf("Ana Garcia") }
    var role by remember { mutableStateOf("Estudiante") }
    var major by remember { mutableStateOf("Ingeniería de Software") }
    val studentId = "ID: 20231345"

    val isDarkMode = MaterialTheme.colorScheme.background == WayDarkBackground

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Perfil",
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
                    IconButton(onClick = { isEditing = !isEditing }) {
                        Icon(
                            imageVector = if (isEditing) Icons.Outlined.Check else Icons.Outlined.Edit,
                            contentDescription = if (isEditing) "Guardar" else "Editar",
                            tint = WayPurple
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(20.dp))

                // Profile Image and Info Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.profile_photo),
                        contentDescription = "Foto de Perfil",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .border(
                                width = 2.dp,
                                color = WayPurple.copy(alpha = 0.2f),
                                shape = CircleShape
                            ),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.width(20.dp))

                    Column {
                        if (isEditing) {
                            OutlinedTextField(
                                value = name,
                                onValueChange = { name = it },
                                modifier = Modifier.fillMaxWidth(),
                                label = { Text("Nombre") },
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = role,
                                onValueChange = { role = it },
                                modifier = Modifier.fillMaxWidth(),
                                label = { Text("Rol") },
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = major,
                                onValueChange = { major = it },
                                modifier = Modifier.fillMaxWidth(),
                                label = { Text("Carrera") },
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp)
                            )
                        } else {
                            Text(
                                text = name,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = role,
                                fontSize = 14.sp,
                                color = WayTextSecondary
                            )
                            Text(
                                text = major,
                                fontSize = 14.sp,
                                color = WayTextSecondary
                            )
                            Text(
                                text = studentId,
                                fontSize = 14.sp,
                                color = WayPurple,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Stats Section
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StatItem(number = "12", label = "Publicaciones")
                    VerticalDivider(modifier = Modifier.height(30.dp), color = WayBorder)
                    StatItem(number = "8", label = "Coincidencias")
                    VerticalDivider(modifier = Modifier.height(30.dp), color = WayBorder)
                    StatItem(number = "5", label = "Objetos devueltos")
                }

                Spacer(modifier = Modifier.height(32.dp))
                HorizontalDivider(color = WayBorder.copy(alpha = 0.5f))
                Spacer(modifier = Modifier.height(16.dp))

                // Menu Items
                ProfileMenuItem(
                    icon = Icons.Outlined.Home,
                    text = "Mis publicaciones",
                    isDarkMode = isDarkMode,
                    onClick = onMyPublicationsClick
                )
                ProfileMenuItem(
                    icon = Icons.AutoMirrored.Outlined.Chat,
                    text = "Mis mensajes",
                    isDarkMode = isDarkMode,
                    onClick = onMyMessagesClick
                )
                ProfileMenuItem(
                    icon = Icons.Outlined.BookmarkBorder,
                    text = "Objetos guardados",
                    isDarkMode = isDarkMode
                )
                ProfileMenuItem(
                    icon = Icons.Outlined.Notifications,
                    text = "Notificaciones",
                    isDarkMode = isDarkMode,
                    onClick = onNotificationsClick
                )
                ProfileMenuItem(
                    icon = Icons.Outlined.LocationOn,
                    text = "Lugares frecuentes",
                    isDarkMode = isDarkMode
                )
                ProfileMenuItem(
                    icon = Icons.Outlined.HelpOutline,
                    text = "Ayuda y soporte",
                    isDarkMode = isDarkMode
                )
                ProfileMenuItem(
                    icon = Icons.Outlined.Info,
                    text = "Acerca de WAY",
                    isDarkMode = isDarkMode
                )

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Composable
fun StatItem(number: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = number,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = WayPurple
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = WayTextSecondary
        )
    }
}

@Composable
fun ProfileMenuItem(
    icon: ImageVector,
    text: String,
    isDarkMode: Boolean,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text,
            modifier = Modifier.weight(1f),
            fontSize = 15.sp,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Medium
        )
        Icon(
            imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
            contentDescription = null,
            tint = WayTextMuted,
            modifier = Modifier.size(20.dp)
        )
    }
}
