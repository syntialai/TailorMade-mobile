package com.future.tailormade.core.model.response.history

data class OrderResponse(

    val createdAt: Long,

    val design: OrderDesignResponse,

    val id: String,

    val quantity: Int,

    val status: String,

    val tailorId: String,

    val totalDiscount: Double,

    val totalPrice: Double,

    val updatedAt: Long,

    val userId: String
) {

  fun getMockResponse() = this.copy(id = "HISTORY_1", createdAt = 1609644391, updatedAt = 160964500,
      quantity = 1, status = "Accepted", tailorId = "TAILOR_1", userId = "USER_1",
      totalPrice = 50000.0, totalDiscount = 0.0, design = this.design)
}