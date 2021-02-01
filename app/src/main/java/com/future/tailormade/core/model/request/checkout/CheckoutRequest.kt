package com.future.tailormade.core.model.request.checkout

data class CheckoutRequest(

    val measurements: CheckoutMeasurementRequest,

    val specialInstructions: String
)