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
)