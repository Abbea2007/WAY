package com.example.wayapp.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wayapp.R
import com.example.wayapp.ui.theme.*

data class ChatConversation(
    val id: String,
    val name: String,
    val lastMessage: String,
    val time: String,
    @DrawableRes val avatar: Int,
    val unreadCount: Int = 0
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyMessagesScreen(
    onBack: () -> Unit = {},
    onChatClick: (String) -> Unit = {}
) {
    val conversations = listOf(
        ChatConversation("1", "Juan Pérez", "Claro, ¿te parece en la biblioteca a las 2 pm?", "10:15 a.m.", R.drawable.imagen_jose, 1),
        ChatConversation("2", "María García", "De nada, te las dejo en recepción.", "Ayer", R.drawable.imagen_ana),
        ChatConversation("3", "Carlos Sosa", "¡Exacto! ¿Dónde nos podemos ver?", "2 may", R.drawable.imagen_carlos),
        ChatConversation("4", "Elena Torres", "Muchas gracias, iré por ella ahora mismo.", "Lunes", R.drawable.imagen_juana),
        ChatConversation("5", "Roberto Ruiz", "Perfecto, ahí te veo.", "30 abr", R.drawable.imagen_pedro)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Mis mensajes",
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
                    IconButton(onClick = { /* Acción de búsqueda */ }) {
                        Icon(
                            imageVector = Icons.Outlined.Search,
                            contentDescription = "Buscar",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
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
        ) {
            items(conversations) { chat ->
                ConversationItem(chat, onClick = { onChatClick(chat.id) })
                HorizontalDivider(
                    modifier = Modifier.padding(start = 84.dp, end = 24.dp),
                    color = WayBorder.copy(alpha = 0.3f)
                )
            }
        }
    }
}

@Composable
fun ConversationItem(chat: ChatConversation, onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar con borde circular sutil
        Image(
            painter = painterResource(id = chat.avatar),
            contentDescription = null,
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = chat.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = chat.time,
                    fontSize = 11.sp,
                    color = WayTextMuted
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = chat.lastMessage,
                    fontSize = 14.sp,
                    color = WayTextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                if (chat.unreadCount > 0) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(WayPurple),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = chat.unreadCount.toString(),
                            color = WayWhite,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
