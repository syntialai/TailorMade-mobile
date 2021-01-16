package com.future.tailormade.core.model.request.checkout

data class CheckoutMeasurementRequest(
		
    val chest: Double,

    val hips: Double,

    val inseam: Double,

    val neckToWaist: Double,

    val waist: Double
)