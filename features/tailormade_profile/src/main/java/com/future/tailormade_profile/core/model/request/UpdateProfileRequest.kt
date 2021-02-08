package com.future.tailormade_profile.core.model.request

import com.future.tailormade_profile.core.model.entity.Location

data class UpdateProfileRequest (

    val name: String,

    val birthDate: Long,

    val phoneNumber: String? = null,

    val location: Location? = null
)