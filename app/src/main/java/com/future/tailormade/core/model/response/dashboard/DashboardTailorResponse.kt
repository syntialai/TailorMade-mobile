package com.future.tailormade.core.model.response.dashboard

import com.future.tailormade_profile.core.model.entity.Location

data class DashboardTailorResponse(

    val id: String,

    val name: String,

    val image: String? = null,

    val location: Location,

    val designs: List<String>? = null
)
