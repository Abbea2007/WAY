package com.example.wayapp.model

data class Chat(
    val chatId: String = "",
    val otherUserId: String = "",
    val otherUserName: String = "",
    val lastMessage: String = "",
    val lastTimestamp: Long = 0L
)