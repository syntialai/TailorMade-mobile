package com.future.tailormade.core.model.response.checkout

data class CheckoutDesignResponse(

    val color: String,

    val discount: Double = 0.0,

    val id: String,

    val image: String,

    val price: Double,

    val size: String,

    val title: String)