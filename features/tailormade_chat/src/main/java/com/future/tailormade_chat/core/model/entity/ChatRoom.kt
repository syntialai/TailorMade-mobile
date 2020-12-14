package com.future.tailormade_chat.core.model.entity

data class ChatRoom(

    val users: MutableMap<String, User>,

    val chats: MutableMap<String, Chat>
)
