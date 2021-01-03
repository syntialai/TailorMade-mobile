package com.future.tailormade.core.model.response.cart

import com.google.gson.annotations.Expose

data class CartResponse(

    val createdAt: String,

    val design: CartDesignResponse,

    val id: String,

    val quantity: Int,

    val tailorId: String,

    val updatedAt: String,

    val userId: String,

    @Expose
    val tailorName: String? = null,

    @Expose
    val userName: String? = null) {

  fun getMockResponse() = this.copy(createdAt = "1609644391", id = "CART_1", quantity = 1,
      tailorId = "TAILOR_1", updatedAt = "1609644400", userId = "USER_1",
      tailorName = "Tailorrrr", userName = "Userrrrr",
      design = CartDesignResponse("", 0.0, "", "", 0.0, "", null, "").getMockResponse())
}