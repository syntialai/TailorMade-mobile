package com.future.tailormade_chat.core.model.entity

import java.sql.Timestamp

data class Session(

    val updatedDate: Timestamp,

    val userId: String,

    val userName: String,

    val chat: Chat,

    val hasBeenRead: Boolean
)
