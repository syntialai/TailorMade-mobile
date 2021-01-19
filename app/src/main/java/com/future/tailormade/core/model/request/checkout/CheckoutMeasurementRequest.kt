package com.future.tailormade.core.model.request.checkout

data class CheckoutMeasurementRequest(
		
    val chest: Float,

    val hips: Float,

    val inseam: Float,

    val neckToWaist: Float,

    val waist: Float
)