package com.future.tailormade.core.model.request.checkout

data class CheckoutRequest(

    val measurement: CheckoutMeasurementRequest,

    val specialInstructions: String? = null
)