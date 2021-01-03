package com.future.tailormade.core.model.response.history

import com.google.gson.annotations.Expose

data class OrderDesignResponse(

    val color: String,

    val discount: Double,

    val id: String,

    val image: String,

    val price: Double,

    val size: String,

    @Expose
    val tailorId: String? = null,

    @Expose
    val tailorName: String? = null,

    val title: String) {

  fun getMockResponse() = this.copy(id = "DESIGN_1",
      image = "https://www.talkwalker.com/images/2020/blog-headers/image-analysis.png",
      color = "Blue", discount = 0.0, price = 50000.0, size = "S",
      title = "Design 1", tailorId = "TAILOR_1", tailorName = "Syntia")
}