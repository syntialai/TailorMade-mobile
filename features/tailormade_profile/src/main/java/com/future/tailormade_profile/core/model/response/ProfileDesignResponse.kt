package com.future.tailormade_profile.core.model.response

data class ProfileDesignResponse(

    val id: String,

    val title: String,

    val image: String,

    val price: Double,

    val discount: Double,

    val active: Boolean
)