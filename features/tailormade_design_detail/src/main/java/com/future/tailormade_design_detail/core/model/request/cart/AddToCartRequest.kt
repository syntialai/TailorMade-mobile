package com.future.tailormade_design_detail.core.model.request.cart

data class AddToCartRequest(

    val userName: String,

    val tailorId: String,

    val tailorName: String,

    val quantity: Int,

    val design: AddToCartDesignRequest)