package com.future.tailormade.tailor_app.core.model.response.order

data class OrderDesignResponse(

    val color: String,

    val discount: Double,

    val id: String,

    val image: String,

    val price: Double,

    val size: String,

    val title: String
)