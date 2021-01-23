package com.future.tailormade.core.model.response.cart

data class CartEditQuantityResponse(

    val id: String,

    val quantity: Int) {

  fun getMockResponse() = this.copy(id = "CART_1", quantity = 5)
}