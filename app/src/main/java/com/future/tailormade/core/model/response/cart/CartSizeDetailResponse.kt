package com.future.tailormade.core.model.response.cart

data class CartSizeDetailResponse(

    val chest: Double? = null,

    val waist: Double? = null,

    val hips: Double? = null,

    val neckToWaist: Double? = null,

    val inseam: Double? = null) {

  fun getMockResponse() = this.copy(chest = 100.0, waist = 150.0, hips = 20.0, neckToWaist = 50.0,
      inseam = 75.0)
}