package com.future.tailormade_chat.core.model.entity

import com.google.firebase.database.IgnoreExtraProperties
import java.sql.Timestamp

@IgnoreExtraProperties
data class LastConnection(

    var lastConnectionDate: Timestamp? = null
)
