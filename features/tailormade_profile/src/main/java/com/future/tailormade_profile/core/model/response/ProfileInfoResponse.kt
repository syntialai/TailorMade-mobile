package com.future.tailormade_profile.core.model.response

import com.future.tailormade_profile.core.model.entity.Education
import com.future.tailormade_profile.core.model.entity.Location
import com.future.tailormade_profile.core.model.entity.Occupation
import com.google.gson.annotations.Expose

data class ProfileInfoResponse(

    val id: String,

    val name: String,

    val image: String? = null,

    val phoneNumber: String? = null,

    val birthDate: Long = 0L,

    val location: Location? = null,

    val occupation: Occupation? = null,

    val education: Education? = null
)