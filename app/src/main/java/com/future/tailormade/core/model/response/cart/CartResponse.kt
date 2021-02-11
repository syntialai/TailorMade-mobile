package com.future.tailormade.core.model.response.cart

import com.google.gson.annotations.Expose

data class CartResponse(

    val createdAt: Long,

    val design: CartDesignResponse,

    val id: String,

    val quantity: Int,

    val tailorId: String,

    val updatedAt: Long,

    val userId: String,

    @Expose
    val tailorName: String? = null,

    @Expose
    val userName: String? = null)