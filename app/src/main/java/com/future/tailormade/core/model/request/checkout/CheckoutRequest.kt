package com.future.tailormade.core.model.request.checkout

data class CheckoutRequest(

    val design: CheckoutDesignRequest,

    val measurement: CheckoutMeasurementRequest,

    val quantity: Int,

    val specialInstructions: String = "",

    val tailorId: String,

    val tailorName: String,

    val totalDiscount: Double,

    val totalPrice: Double,

    val userId: String,

    val userName: String
)