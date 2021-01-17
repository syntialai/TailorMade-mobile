package com.future.tailormade_chat.core.model.entity

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(

    var name: String? = null
)
