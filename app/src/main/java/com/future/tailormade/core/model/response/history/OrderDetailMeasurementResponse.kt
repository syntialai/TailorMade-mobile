package com.future.tailormade.core.model.response.history

data class OrderDetailMeasurementResponse(
    val chest: Int,
    val hips: Int,
    val inseam: Int,
    val neckToWaist: Int,
    val waist: Int
)