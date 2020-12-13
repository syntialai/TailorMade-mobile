package com.future.tailormade.core.model.response.cart

import com.google.gson.annotations.Expose

data class CartDesignResponse(

    val color: String,

    val discount: Double,

    val id: String,

    val image: String,

    val price: Double,

    val size: String,

		@Expose
		val sizeDetail: CartSizeDetailResponse? = null,

    val title: String
)