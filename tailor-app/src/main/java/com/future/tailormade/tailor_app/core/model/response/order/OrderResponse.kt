package com.future.tailormade.tailor_app.core.model.response.order

data class OrderResponse(

    val createdAt: String,

    val design: OrderDesignResponse,

    val id: String,

    val quantity: Int,

    val specialInstructions: String,

    val status: String,

    val tailorId: String,

    val totalDiscount: Double,

    val totalPrice: Double,

    val updatedAt: String,

    val userId: String
)