package com.future.tailormade.core.model.response.dashboard

data class DashboardTailorResponse(

    val id: String,

    val name: String,

    val image: String? = null,

    val location: DashboardLocationResponse? = null,

    val designs: List<DashboardDesignResponse>? = null)
