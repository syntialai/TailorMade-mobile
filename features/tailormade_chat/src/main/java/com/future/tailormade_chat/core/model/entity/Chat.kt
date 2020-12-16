package com.future.tailormade_chat.core.model.entity

import com.google.gson.annotations.Expose
import java.sql.Timestamp

data class Chat(

    @Expose
    val createdDate: Timestamp,

    @Expose
    val userId: String,

    @Expose
    val hasBeenRead: Boolean,

    val type: String,

    val text: Text
)
