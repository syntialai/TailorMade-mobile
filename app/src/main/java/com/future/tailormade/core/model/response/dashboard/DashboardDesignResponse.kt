package com.future.tailormade.core.model.response.dashboard

data class DashboardDesignResponse(

    val id: String,

    val image: String) {

  fun getMockResponse() = this.copy(id = "DESIGN_1",
      image = "https://www.talkwalker.com/images/2020/blog-headers/image-analysis.png")
}
