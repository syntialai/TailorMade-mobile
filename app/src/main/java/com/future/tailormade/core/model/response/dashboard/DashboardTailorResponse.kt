package com.future.tailormade.core.model.response.dashboard

data class DashboardTailorResponse(

    val id: String,

    val name: String,

    val image: String? = null,

    val location: DashboardLocationResponse? = null,

    val designs: List<DashboardDesignResponse>? = null) {

  fun getMockResponse() = this.copy(id = "UUID_1", name = "Syntia",
      image = "https://miro.medium.com/max/1200/1*mk1-6aYaf_Bes1E3Imhc0A.jpeg",
      location = this.location?.getMockResponse(),
      designs = listOf(DashboardDesignResponse("", "").getMockResponse()))
}
