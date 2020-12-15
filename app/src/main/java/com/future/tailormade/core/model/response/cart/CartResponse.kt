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
		val userName: String? = null,
)