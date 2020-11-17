package com.future.tailormade_profile.core.model.request

data class UpdateProfileRequest (

    val name: String,

    val birthDate: Long,

    val phoneNumber: String? = null,

    val location: String? = null
)