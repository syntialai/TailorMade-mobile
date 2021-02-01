package com.future.tailormade.core.model.request.checkout

data class CheckoutMeasurementRequest(

    val chest: Float,

    val waist: Float,

    val hips: Float,

    val neckToWaist: Float,

    val inseam: Float)