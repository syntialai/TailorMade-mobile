package com.future.tailormade.core.model.response.dashboard

data class DashboardLocationResponse(

    val city: String? = null,

    val country: String? = null) {

  fun getMockResponse() = this.copy(city = "Medan", country = "Indonesia")
}