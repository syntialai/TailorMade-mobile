package com.future.tailormade.core.model.request.checkout

data class CheckoutDesignRequest(

    val color: String,

    val discount: Double,

    val id: String,

    val image: String,

    val price: Double,

    val size: String,

    val tailorId: String,

    val tailorName: String,

    val title: String
)