package com.example.wayapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.wayapp.data.FirestoreManager
import com.example.wayapp.model.Chat
import com.example.wayapp.ui.theme.WayBorder
import com.example.wayapp.ui.theme.WayPurple
import com.example.wayapp.ui.theme.WayTextMuted
import com.example.wayapp.ui.theme.WayTextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyMessagesScreen(
    onBack: () -> Unit = {},
    onChatClick: (
        otherUserId: String,
        otherUserName: String
    ) -> Unit = { _, _ -> }
) {
    val firestoreManager = remember {
        FirestoreManager()
    }

    var chats by remember {
        mutableStateOf<List<Chat>>(emptyList())
    }

    var isLoading by remember {
        mutableStateOf(true)
    }

    var errorMessage by remember {
        mutableStateOf<String?>(null)
    }

    DisposableEffect(Unit) {
        val registration = firestoreManager.listenMyChats(
            onChange = { nuevaLista ->
                chats = nuevaLista
                isLoading = false
                errorMessage = null
            },
            onError = { error ->
                isLoading = false

                /*
                 * Si ya había chats visibles, no los eliminamos.
                 */
                if (chats.isEmpty()) {
                    errorMessage = error.message
                        ?: "No se pudieron cargar las conversaciones."
                }
            }
        )

        onDispose {
            registration.remove()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Mis mensajes",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme
                            .colorScheme
                            .onBackground
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack
                    ) {
                        Icon(
                            imageVector = Icons
                                .AutoMirrored
                                .Outlined
                                .ArrowBack,
                            contentDescription = "Atrás",
                            tint = MaterialTheme
                                .colorScheme
                                .onBackground
                        )
                    }
                },
                colors = TopAppBarDefaults
                    .topAppBarColors(
                        containerColor = MaterialTheme
                            .colorScheme
                            .background
                    )
            )
        },
        containerColor = MaterialTheme
            .colorScheme
            .background
    ) { paddingValues ->

        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = WayPurple
                    )
                }
            }

            chats.isEmpty() && errorMessage != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = errorMessage
                            ?: "No se pudieron cargar las conversaciones.",
                        color = MaterialTheme
                            .colorScheme
                            .error,
                        fontSize = 14.sp
                    )
                }
            }

            chats.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Todavía no tienes conversaciones.",
                        color = WayTextMuted,
                        fontSize = 14.sp
                    )
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    items(
                        items = chats,
                        key = { chat ->
                            chat.chatId
                        }
                    ) { chat ->

                        ConversationItem(
                            chat = chat,
                            onClick = {
                                onChatClick(
                                    chat.otherUserId,
                                    chat.otherUserName
                                )
                            }
                        )

                        HorizontalDivider(
                            modifier = Modifier.padding(
                                start = 84.dp,
                                end = 24.dp
                            ),
                            color = WayBorder.copy(
                                alpha = 0.3f
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ConversationItem(
    chat: Chat,
    onClick: () -> Unit = {}
) {
    val horaFormateada = remember(
        chat.lastTimestamp
    ) {
        if (chat.lastTimestamp == 0L) {
            ""
        } else {
            val formatter = SimpleDateFormat(
                "dd/MM, HH:mm",
                Locale.getDefault()
            )

            formatter.format(
                Date(chat.lastTimestamp)
            )
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(
                horizontal = 24.dp,
                vertical = 16.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(
                id = R.drawable.profile_photo
            ),
            contentDescription = "Foto de perfil",
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(
            modifier = Modifier.width(16.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.SpaceBetween,
                verticalAlignment =
                    Alignment.CenterVertically
            ) {
                Text(
                    text = chat.otherUserName
                        .ifBlank { "Usuario" },
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme
                        .colorScheme
                        .onBackground,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(
                    modifier = Modifier.width(8.dp)
                )

                Text(
                    text = horaFormateada,
                    fontSize = 11.sp,
                    color = WayTextMuted
                )
            }

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = chat.lastMessage.ifBlank {
                    "Conversación iniciada"
                },
                fontSize = 14.sp,
                color = WayTextSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}