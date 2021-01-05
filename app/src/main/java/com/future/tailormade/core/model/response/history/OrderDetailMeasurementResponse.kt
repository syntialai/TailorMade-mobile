package com.future.tailormade.core.model.response.history

data class OrderDetailMeasurementResponse(

    val chest: Float,

    val hips: Float,

    val inseam: Float,

    val neckToWaist: Float,

    val waist: Float) {

  fun getMockResponse() = this.copy(chest = 98.4f, hips = 40f, inseam = 50.0f, neckToWaist = 48f,
      waist = 70.5f)
}