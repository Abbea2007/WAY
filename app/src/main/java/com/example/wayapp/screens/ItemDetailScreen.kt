package com.example.wayapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wayapp.R
import com.example.wayapp.ui.theme.*

@Composable
fun ItemDetailScreen(
    itemId: String,
    onBack: () -> Unit = {}
) {
    // Datos de prueba según el ID
    val item = when (itemId) {
        "1" -> LostItem(
            "1", "Audífonos inalámbricos", "Encontrado", true, "Hoy, 10:30 a.m.", "Biblioteca Central", R.drawable.rectangle17,
            "Se encontraron estos audífonos en la sala de lectura. Estaban sobre la mesa.", "Apple", "Blanco", "Buen estado"
        )
        "2" -> LostItem(
            "2", "Mochila Negra", "Perdido", false, "Ayer, 6:45 p.m.", "Edificio A", R.drawable.rectangle18,
            "Perdí mi mochila con mis cuadernos cerca de la entrada principal del edificio.", "Nike", "Negro", "Usado"
        )
        "3" -> LostItem(
            "3", "Llaves de carro", "Perdido", false, "Hace 2 horas", "Estacionamiento B", R.drawable.rectangle19,
            "Se me cayeron las llaves al bajar del auto. Tienen un llavero de metal con forma de corazón.", "Toyota", "Plateado", "Excelente"
        )
        "4" -> LostItem(
            "4", "Termo para café", "Encontrado", true, "Hoy, 8:15 a.m.", "Cafetería Central", R.drawable.rectangle20,
            "Olvidaron este termo en una de las mesas exteriores cerca de la fuente.", "Starbucks", "Azul marino", "Como nuevo"
        )
        else -> LostItem(
            "5", "Billetera", "Encontrado", true, "Lunes, 4:00 p.m.", "Gimnasio", R.drawable.rectangle21,
            "Billetera de cuero encontrada en los vestidores del gimnasio.", "Tommy Hilfiger", "Café", "Desgastada"
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Main Scrollable Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Image Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp)
            ) {
                Image(
                    painter = painterResource(id = item.image),
                    contentDescription = item.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Top Buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 48.dp, start = 20.dp, end = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.9f))
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Atrás",
                            tint = Color.Black
                        )
                    }

                    IconButton(
                        onClick = { /* Opciones */ },
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.9f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreHoriz,
                            contentDescription = "Más",
                            tint = Color.Black
                        )
                    }
                }
            }

            // Information Content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-20).dp)
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .background(MaterialTheme.colorScheme.background)
                    .padding(24.dp)
            ) {
                // Status Badge
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = if (item.isFound) WayGreenSoft else WayRed.copy(alpha = 0.1f),
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(if (item.isFound) WayGreen else WayRed)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = item.status,
                            color = if (item.isFound) WayGreenDark else WayRed,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Text(
                    text = item.title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.Schedule,
                        contentDescription = null,
                        tint = WayPurple,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = item.time,
                        fontSize = 14.sp,
                        color = WayTextSecondary
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(verticalAlignment = Alignment.Top) {
                    Icon(
                        imageVector = Icons.Outlined.LocationOn,
                        contentDescription = null,
                        tint = WayPurple,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = item.location,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = if (item.id == "1") "Sala de lectura 2, mesa 14" else "Área común, planta baja",
                            fontSize = 13.sp,
                            color = WayTextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                HorizontalDivider(color = WayBorder.copy(alpha = 0.5f))
                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Descripción",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = item.description,
                    fontSize = 14.sp,
                    color = WayTextSecondary,
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Información adicional",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(16.dp))

                InfoRow("Marca", item.brand)
                InfoRow("Color", item.color)
                InfoRow("Estado", item.state)

                Spacer(modifier = Modifier.height(100.dp))
            }
        }

        // Bottom Button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(MaterialTheme.colorScheme.background)
                .padding(24.dp)
        ) {
            PrimaryAuthButton(
                text = "Tengo más información",
                onClick = { /* Acción */ }
            )
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = value,
            fontSize = 14.sp,
            color = WayTextSecondary
        )
    }
}
