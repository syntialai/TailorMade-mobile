package com.future.tailormade_chat.core.model.entity

import com.google.firebase.database.IgnoreExtraProperties
import java.sql.Timestamp

@IgnoreExtraProperties
data class Session(

    var id: String? = null,

    var updatedDate: Timestamp? = null,

    var userId: String? = null,

    var userName: String? = null,

    var chat: Chat? = null,

    var hasBeenRead: Boolean? = null
)
