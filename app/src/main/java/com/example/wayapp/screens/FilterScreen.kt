package com.example.wayapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wayapp.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreen(
    onBack: () -> Unit = {},
    onApply: () -> Unit = {}
) {
    var selectedType by remember { mutableIntStateOf(0) }
    val isDarkMode = MaterialTheme.colorScheme.background == WayDarkBackground

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Filtros",
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
                    TextButton(onClick = { /* Limpiar filtros */ }) {
                        Text(
                            text = "Limpiar",
                            color = WayPurple,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                PrimaryAuthButton(
                    text = "Aplicar filtros",
                    onClick = onApply
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Tipo Section
            FilterSection(title = "Tipo") {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    TypeChip("Todos", selected = selectedType == 0, onClick = { selectedType = 0 })
                    TypeChip("Perdidos", selected = selectedType == 1, onClick = { selectedType = 1 })
                    TypeChip("Encontrados", selected = selectedType == 2, onClick = { selectedType = 2 })
                }
            }

            // Categoría Section
            FilterSection(title = "Categoría") {
                DropdownField(placeholder = "Selecciona una categoría", isDarkMode = isDarkMode)
            }

            // Ubicación Section
            FilterSection(title = "Ubicación") {
                DropdownField(placeholder = "Selecciona una ubicación", isDarkMode = isDarkMode)
            }

            // Fecha Section
            FilterSection(title = "Fecha") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    DateField(placeholder = "Desde", modifier = Modifier.weight(1f), isDarkMode = isDarkMode)
                    DateField(placeholder = "Hasta", modifier = Modifier.weight(1f), isDarkMode = isDarkMode)
                }
            }

            // Estado Section
            FilterSection(title = "Estado") {
                DropdownField(placeholder = "Todos", isDarkMode = isDarkMode)
            }

            Spacer(modifier = Modifier.height(100.dp)) // Espacio para el botón inferior
        }
    }
}

@Composable
fun FilterSection(title: String, content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = title,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        content()
    }
}

@Composable
fun TypeChip(text: String, selected: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .clickable { onClick() }
            .clip(RoundedCornerShape(20.dp)),
        color = if (selected) WayPurple else Color(0xFFF3F4F6),
        shape = RoundedCornerShape(20.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
            fontSize = 14.sp,
            color = if (selected) WayWhite else WayTextSecondary,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
        )
    }
}

@Composable
fun DropdownField(placeholder: String, isDarkMode: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.surface)
            .border(
                width = 1.dp,
                color = if (isDarkMode) MaterialTheme.colorScheme.outline else WayBorder,
                shape = RoundedCornerShape(14.dp)
            )
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = placeholder,
                color = WayTextMuted,
                fontSize = 14.sp
            )
            Icon(
                imageVector = Icons.Outlined.KeyboardArrowDown,
                contentDescription = null,
                tint = WayTextMuted
            )
        }
    }
}

@Composable
fun DateField(placeholder: String, modifier: Modifier = Modifier, isDarkMode: Boolean) {
    Box(
        modifier = modifier
            .height(56.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.surface)
            .border(
                width = 1.dp,
                color = if (isDarkMode) MaterialTheme.colorScheme.outline else WayBorder,
                shape = RoundedCornerShape(14.dp)
            )
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = placeholder,
                color = WayTextMuted,
                fontSize = 14.sp
            )
            Icon(
                imageVector = Icons.Outlined.CalendarMonth,
                contentDescription = null,
                tint = WayPurple,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
