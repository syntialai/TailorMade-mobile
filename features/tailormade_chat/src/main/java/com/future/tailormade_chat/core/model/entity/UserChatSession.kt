package com.future.tailormade_chat.core.model.entity

data class UserChatSession(

    val userId: String,

    val userName: String,

    val hasBeenRead: Boolean,

    val unreadRoomCount: Int,

    val sessions: MutableMap<String, Session>
)
