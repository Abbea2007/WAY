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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wayapp.R
import com.example.wayapp.data.FirestoreManager
import com.example.wayapp.ui.theme.*
import com.example.wayapp.viewmodel.HomeViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ItemDetailScreen(
    itemId: String,
    onBack: () -> Unit = {},
    onContactClick: (otherUserId: String, otherUserName: String) -> Unit = { _, _ -> },
    viewModel: HomeViewModel = viewModel()
) {
    // 1. Observamos el objeto específico desde la base de datos
    val objeto by viewModel.obtenerObjetoPorId(itemId).collectAsState(initial = null)

    // 2. Pantalla de carga mientras se lee de la base de datos
    if (objeto == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = WayPurple)
        }
        return // Pausamos la ejecución del diseño hasta que el objeto cargue
    }

    val firestoreManager = remember { FirestoreManager() }
    val currentUid = remember { FirebaseAuth.getInstance().currentUser?.uid }
    val esMiPropiaPublicacion = objeto!!.idUsuarioReporta.isNotBlank() && objeto!!.idUsuarioReporta == currentUid

    // Nombre del dueño de la publicación (se busca en Firestore una sola vez que carga el objeto)
    var nombreDueno by remember { mutableStateOf("Usuario") }
    LaunchedEffect(objeto!!.idUsuarioReporta) {
        val uidDueno = objeto!!.idUsuarioReporta
        if (uidDueno.isNotBlank()) {
            firestoreManager.obtenerUsuario(uidDueno) { usuario ->
                nombreDueno = usuario?.nombre?.ifBlank { "Usuario" } ?: "Usuario"
            }
        }
    }

    // 3. Mapeamos el dato real a tu modelo visual (LostItem)
    val item = LostItem(
        id = objeto!!.id,
        title = objeto!!.nombre,
        status = if (objeto!!.estado == "PERDIDO") "Perdido" else "Encontrado",
        isFound = objeto!!.estado != "PERDIDO",
        time = objeto!!.fechaHora,
        location = objeto!!.ubicacion,
        image = R.drawable.rectangle17, // Imagen por defecto mientras integramos fotos
        description = objeto!!.descripcion,
        brand = objeto!!.categoria
    )

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
                            text = "Ubicación registrada",
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
            if (esMiPropiaPublicacion) {
                PrimaryAuthButton(
                    text = "Esta es tu publicación",
                    onClick = { }
                )
            } else {
                PrimaryAuthButton(
                    text = "Tengo más información",
                    onClick = {
                        val uidDueno = objeto!!.idUsuarioReporta
                        if (uidDueno.isNotBlank()) {
                            onContactClick(uidDueno, nombreDueno)
                        }
                    }
                )
            }
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