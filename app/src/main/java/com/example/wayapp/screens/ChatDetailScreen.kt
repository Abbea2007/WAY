package com.example.wayapp.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
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
import com.example.wayapp.R
import com.example.wayapp.ui.theme.*

data class Message(
    val text: String,
    val time: String,
    val isFromMe: Boolean
)

data class ChatData(
    val userName: String,
    @DrawableRes val userAvatar: Int,
    val itemName: String,
    val itemStatus: String,
    val itemTime: String,
    @DrawableRes val itemImage: Int,
    val messages: List<Message>
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatDetailScreen(
    chatId: String,
    onBack: () -> Unit = {}
) {
    val chatData = remember(chatId) { getChatData(chatId) }
    var messageText by remember { mutableStateOf("") }
    val isDarkMode = MaterialTheme.colorScheme.background == WayDarkBackground

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = chatData.userAvatar),
                            contentDescription = null,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = chatData.userName,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = "Activo ahora",
                                fontSize = 12.sp,
                                color = WayGreen,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        bottomBar = {
            ChatInputBar(
                value = messageText,
                onValueChange = { messageText = it },
                onSend = { messageText = "" },
                isDarkMode = isDarkMode
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Item Info Card
            ItemInfoCard(chatData, isDarkMode)

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(chatData.messages) { message ->
                    ChatMessageBubble(message, chatData.userAvatar, isDarkMode)
                }
            }
        }
    }
}

@Composable
fun ItemInfoCard(chatData: ChatData, isDarkMode: Boolean) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = chatData.itemImage),
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = chatData.itemName,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "${chatData.itemStatus} • ${chatData.itemTime}",
                    fontSize = 13.sp,
                    color = if (chatData.itemStatus.contains("Perdida", ignoreCase = true)) WayRed else WayGreen
                )
            }
        }
    }
}

@Composable
fun ChatMessageBubble(message: Message, userAvatar: Int, isDarkMode: Boolean) {
    val alignment = if (message.isFromMe) Alignment.End else Alignment.Start
    val bubbleColor = if (message.isFromMe) WayPurple else if (isDarkMode) Color(0xFF252B35) else Color(0xFFF3F4F6)
    val textColor = if (message.isFromMe) WayWhite else MaterialTheme.colorScheme.onBackground

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = alignment
    ) {
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = if (message.isFromMe) Arrangement.End else Arrangement.Start
        ) {
            if (!message.isFromMe) {
                Image(
                    painter = painterResource(id = userAvatar),
                    contentDescription = null,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(8.dp))
            }

            Surface(
                color = bubbleColor,
                shape = RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp,
                    bottomStart = if (message.isFromMe) 16.dp else 4.dp,
                    bottomEnd = if (message.isFromMe) 4.dp else 16.dp
                )
            ) {
                Text(
                    text = message.text,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    color = textColor,
                    fontSize = 14.sp
                )
            }
        }
        Text(
            text = message.time,
            fontSize = 11.sp,
            color = WayTextMuted,
            modifier = Modifier.padding(top = 4.dp, start = if (message.isFromMe) 0.dp else 40.dp)
        )
    }
}

@Composable
fun ChatInputBar(
    value: String,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit,
    isDarkMode: Boolean
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .imePadding(),
        color = MaterialTheme.colorScheme.background,
        tonalElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 48.dp),
                placeholder = { Text("Escribe un mensaje...", fontSize = 14.sp, color = WayTextMuted) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = if (isDarkMode) Color(0xFF1A1F27) else Color(0xFFF3F4F6),
                    unfocusedContainerColor = if (isDarkMode) Color(0xFF1A1F27) else Color(0xFFF3F4F6),
                    disabledContainerColor = if (isDarkMode) Color(0xFF1A1F27) else Color(0xFFF3F4F6),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                ),
                shape = RoundedCornerShape(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            IconButton(
                onClick = onSend,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(WayPurple)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Enviar",
                    tint = WayWhite,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

fun getChatData(chatId: String): ChatData {
    return when (chatId) {
        "1" -> ChatData(
            "Juan Pérez", R.drawable.imagen_jose,
            "Mochila negra", "Perdida", "Ayer, 8:00 p.m.", R.drawable.rectangle18,
            listOf(
                Message("Hola Ana, creo que tengo tu mochila.", "10:15 a.m.", false),
                Message("¡En serio! ¿Dónde la encontraste?", "10:16 a.m.", true),
                Message("En la cafetería, sobre una silla.", "10:17 a.m.", false),
                Message("¿Podríamos vernos para recogerla?", "10:18 a.m.", true),
                Message("Claro, ¿te parece en la biblioteca a las 2 pm?", "10:19 a.m.", false)
            )
        )
        "2" -> ChatData(
            "María García", R.drawable.imagen_ana,
            "Llaves de carro", "Perdida", "Hoy, 9:00 a.m.", R.drawable.rectangle19,
            listOf(
                Message("Hola, ¿tú perdiste unas llaves?", "11:00 a.m.", false),
                Message("Sí, son mías. ¡Muchas gracias por devolverlas!", "11:05 a.m.", true),
                Message("De nada, te las dejo en recepción.", "11:10 a.m.", false)
            )
        )
        "3" -> ChatData(
            "Carlos Sosa", R.drawable.imagen_carlos,
            "Audífonos inalámbricos", "Encontrado", "Ayer, 2:00 p.m.", R.drawable.rectangle17,
            listOf(
                Message("Hola Carlos, vi que encontraste unos audífonos.", "9:00 a.m.", true),
                Message("Hola, sí. ¿Son tuyos?", "9:05 a.m.", false),
                Message("Creo que sí, son unos negros marca Sony.", "9:10 a.m.", true),
                Message("¡Exacto! ¿Dónde nos podemos ver?", "9:15 a.m.", false)
            )
        )
        "4" -> ChatData(
            "Elena Torres", R.drawable.imagen_juana,
            "Libreta con espiral", "Encontrado", "30 abr, 11:10 a.m.", R.drawable.rectangle21,
            listOf(
                Message("Hola, encontré tu libreta en la biblioteca.", "Lunes, 2:00 p.m.", false),
                Message("¡Qué alivio! Pensé que la había perdido para siempre.", "Lunes, 2:05 p.m.", true),
                Message("Está en la recepción del segundo piso.", "Lunes, 2:10 p.m.", false),
                Message("Muchas gracias, iré por ella ahora mismo.", "Lunes, 2:15 p.m.", true)
            )
        )
        "5" -> ChatData(
            "Roberto Ruiz", R.drawable.imagen_pedro,
            "Termo azul marino", "Perdida", "1 may, 4:20 p.m.", R.drawable.rectangle20,
            listOf(
                Message("Hola, ¿sigue disponible el termo azul que publicaste?", "Hoy, 10:00 a.m.", false),
                Message("Hola Roberto, sí, todavía lo tengo.", "Hoy, 10:05 a.m.", true),
                Message("¿Dónde nos podríamos ver para recogerlo?", "Hoy, 10:10 a.m.", false),
                Message("Estaré en el gimnasio a las 4 p.m.", "Hoy, 10:15 a.m.", true),
                Message("Perfecto, ahí te veo.", "Hoy, 10:20 a.m.", false)
            )
        )
        else -> ChatData(
            "Usuario", R.drawable.profile_photo,
            "Objeto", "Reportado", "Recientemente", R.drawable.rectangle21,
            listOf(
                Message("Hola, tengo información sobre tu objeto.", "12:00 p.m.", false),
                Message("Muchas gracias por avisar.", "12:05 p.m.", true)
            )
        )
    }
}
