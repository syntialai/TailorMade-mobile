package com.future.tailormade_chat.core.model.entity

import com.google.firebase.database.IgnoreExtraProperties
import java.sql.Timestamp

@IgnoreExtraProperties
data class Chat(

    var createdDate: Long? = null,

    var userId: String? = null,

    var hasBeenRead: Boolean? = null,

    var type: String? = null,

    var text: Text? = null
)
