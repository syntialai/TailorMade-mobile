package com.future.tailormade_chat.core.model.entity

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class UserChatSession(

    var userId: String? = null,

    var userName: String? = null,

    var hasBeenRead: Boolean? = null,

    var unreadRoomCount: Int? = null,

    var sessions: MutableMap<String, Session>? = null
)
