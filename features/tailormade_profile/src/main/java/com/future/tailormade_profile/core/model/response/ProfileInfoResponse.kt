package com.future.tailormade_profile.core.model.response

import com.future.tailormade_profile.core.model.entity.Location
import com.google.gson.annotations.Expose

data class ProfileInfoResponse(

    @Expose
    val id: String = "",

    val name: String = "",

    val phoneNumber: String? = null,

    val birthDate: Long = 0L,

    val location: Location? = null
)