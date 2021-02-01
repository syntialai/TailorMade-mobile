package com.future.tailormade.tailor_app.core.model.response.order

data class OrderResponse(

    val createdAt: Long,

    val design: OrderDesignResponse,

    val id: String,

    val quantity: Int,

    val specialInstructions: String? = null,

    val status: String,

    val tailorId: String,

    val totalDiscount: Double,

    val totalPrice: Double,

    val updatedAt: Long,

    val userId: String
)