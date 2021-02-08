package com.future.tailormade_chat.core.model.entity

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class ChatRoom(

    var users: MutableMap<String, User>? = null,

    var chats: MutableMap<String, Chat>? = null
)
