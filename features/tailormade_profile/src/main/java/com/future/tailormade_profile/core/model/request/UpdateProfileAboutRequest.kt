package com.future.tailormade_profile.core.model.request

import com.future.tailormade_profile.core.model.entity.Education
import com.future.tailormade_profile.core.model.entity.Occupation

data class UpdateProfileAboutRequest(

    val occupation: Occupation? = null,

    val education: Education? = null
)