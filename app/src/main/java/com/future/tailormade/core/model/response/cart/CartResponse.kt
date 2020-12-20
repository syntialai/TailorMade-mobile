package com.future.tailormade.core.model.response.cart

data class CartResponse(

    val createdAt: String,

    val design: CartDesignResponse,

    val id: String,

    val quantity: Int,

    val tailorId: String,

    val updatedAt: String,

    val userId: String)