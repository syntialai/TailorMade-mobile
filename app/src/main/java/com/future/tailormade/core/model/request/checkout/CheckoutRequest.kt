package com.future.tailormade.core.model.request.checkout

data class CheckoutRequest(

    val design: CheckoutDesignRequest,

    val measurement: CheckoutMeasurementRequest,

    val specialInstructions: String = ""
)