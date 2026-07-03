package com.example.wayapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wayapp.data.FirestoreManager
import com.example.wayapp.model.Message
import com.example.wayapp.ui.theme.WayDarkBackground
import com.example.wayapp.ui.theme.WayPurple
import com.example.wayapp.ui.theme.WayTextMuted
import com.example.wayapp.ui.theme.WayWhite
import com.example.wayapp.viewmodel.UserProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatDetailScreen(
    otherUserId: String,
    otherUserName: String,
    onBack: () -> Unit = {},
    userViewModel: UserProfileViewModel = viewModel()
) {
    val firestoreManager = remember {
        FirestoreManager()
    }

    val currentUid = remember {
        FirebaseAuth
            .getInstance()
            .currentUser
            ?.uid
            .orEmpty()
    }

    val chatId = remember(
        currentUid,
        otherUserId
    ) {
        firestoreManager.getChatId(
            otherUserId
        )
    }

    var messageText by remember {
        mutableStateOf("")
    }

    var messages by remember {
        mutableStateOf<List<Message>>(emptyList())
    }

    var isLoadingMessages by remember {
        mutableStateOf(true)
    }

    var errorMessage by remember {
        mutableStateOf<String?>(null)
    }

    val listState = rememberLazyListState()

    val isDarkMode =
        MaterialTheme.colorScheme.background ==
                WayDarkBackground

    DisposableEffect(chatId) {
        if (chatId.isBlank()) {
            isLoadingMessages = false
            errorMessage =
                "No se pudo identificar la conversación."

            onDispose { }
        } else {
            val registration =
                firestoreManager.listenMessages(
                    chatId = chatId,
                    onChange = { nuevaLista ->
                        messages = nuevaLista
                        isLoadingMessages = false
                        errorMessage = null
                    },
                    onError = { error ->
                        isLoadingMessages = false

                        if (messages.isEmpty()) {
                            errorMessage = error.message
                                ?: "No se pudieron cargar los mensajes."
                        }
                    }
                )

            onDispose {
                registration.remove()
            }
        }
    }

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(
                messages.lastIndex
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = otherUserName.ifBlank {
                            "Usuario"
                        },
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme
                            .colorScheme
                            .onBackground,
                        maxLines = 1
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
        bottomBar = {
            ChatInputBar(
                value = messageText,
                onValueChange = {
                    messageText = it
                },
                onSend = {
                    val mensaje = messageText.trim()

                    if (
                        mensaje.isNotBlank() &&
                        otherUserId.isNotBlank()
                    ) {
                        firestoreManager.sendMessage(
                            otherUserId = otherUserId,
                            otherUserName =
                                otherUserName.ifBlank {
                                    "Usuario"
                                },
                            myName =
                                userViewModel.name.ifBlank {
                                    "Usuario"
                                },
                            text = mensaje
                        )

                        messageText = ""
                    }
                },
                isDarkMode = isDarkMode
            )
        },
        containerColor = MaterialTheme
            .colorScheme
            .background
    ) { paddingValues ->

        when {
            isLoadingMessages -> {
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

            messages.isEmpty() &&
                    errorMessage != null -> {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = errorMessage
                            ?: "No se pudieron cargar los mensajes.",
                        color = MaterialTheme
                            .colorScheme
                            .error,
                        fontSize = 14.sp
                    )
                }
            }

            messages.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Aún no hay mensajes.\n¡Envía el primero!",
                        color = WayTextMuted,
                        fontSize = 14.sp
                    )
                }
            }

            else -> {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 16.dp),
                    verticalArrangement =
                        Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(
                        vertical = 16.dp
                    )
                ) {
                    items(
                        items = messages,
                        key = { message ->
                            message.id
                        }
                    ) { message ->

                        ChatMessageBubble(
                            message = message,
                            isFromMe =
                                message.senderId ==
                                        currentUid,
                            isDarkMode = isDarkMode
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ChatMessageBubble(
    message: Message,
    isFromMe: Boolean,
    isDarkMode: Boolean
) {
    val alignment = if (isFromMe) {
        Alignment.End
    } else {
        Alignment.Start
    }

    val bubbleColor = when {
        isFromMe -> WayPurple
        isDarkMode -> Color(0xFF252B35)
        else -> Color(0xFFF3F4F6)
    }

    val textColor = if (isFromMe) {
        WayWhite
    } else {
        MaterialTheme.colorScheme.onBackground
    }

    val horaFormateada = remember(
        message.timestamp
    ) {
        val formatter = SimpleDateFormat(
            "HH:mm",
            Locale.getDefault()
        )

        formatter.format(
            Date(message.timestamp)
        )
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = alignment
    ) {
        Surface(
            color = bubbleColor,
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isFromMe) {
                    16.dp
                } else {
                    4.dp
                },
                bottomEnd = if (isFromMe) {
                    4.dp
                } else {
                    16.dp
                }
            )
        ) {
            Text(
                text = message.text,
                modifier = Modifier.padding(
                    horizontal = 16.dp,
                    vertical = 10.dp
                ),
                color = textColor,
                fontSize = 14.sp
            )
        }

        Text(
            text = horaFormateada,
            fontSize = 11.sp,
            color = WayTextMuted,
            modifier = Modifier.padding(
                top = 4.dp
            )
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
    val canSend = value.isNotBlank()

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .imePadding(),
        color = MaterialTheme
            .colorScheme
            .background,
        tonalElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment =
                Alignment.CenterVertically
        ) {
            TextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 48.dp),
                placeholder = {
                    Text(
                        text = "Escribe un mensaje...",
                        fontSize = 14.sp,
                        color = WayTextMuted
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor =
                        if (isDarkMode) {
                            Color(0xFF1A1F27)
                        } else {
                            Color(0xFFF3F4F6)
                        },
                    unfocusedContainerColor =
                        if (isDarkMode) {
                            Color(0xFF1A1F27)
                        } else {
                            Color(0xFFF3F4F6)
                        },
                    disabledContainerColor =
                        if (isDarkMode) {
                            Color(0xFF1A1F27)
                        } else {
                            Color(0xFFF3F4F6)
                        },
                    focusedIndicatorColor =
                        Color.Transparent,
                    unfocusedIndicatorColor =
                        Color.Transparent,
                    focusedTextColor =
                        MaterialTheme
                            .colorScheme
                            .onSurface,
                    unfocusedTextColor =
                        MaterialTheme
                            .colorScheme
                            .onSurface
                ),
                shape = RoundedCornerShape(24.dp),
                maxLines = 4
            )

            Spacer(
                modifier = Modifier.width(12.dp)
            )

            IconButton(
                onClick = onSend,
                enabled = canSend,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(
                        if (canSend) {
                            WayPurple
                        } else {
                            WayPurple.copy(
                                alpha = 0.45f
                            )
                        }
                    )
            ) {
                Icon(
                    imageVector = Icons
                        .AutoMirrored
                        .Filled
                        .Send,
                    contentDescription = "Enviar",
                    tint = WayWhite,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}