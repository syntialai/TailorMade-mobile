package com.future.tailormade.core.model.response.checkout

data class CheckoutResponse(

		val id: String,

    val design: CheckoutDesignResponse,

    val quantity: Int,

    val tailorId: String,

    val totalDiscount: Double,

    val totalPrice: Double,

    val userId: String
)