package com.future.tailormade.core.model.ui.history

data class OrderUiModel(

    var orderDate: String,

    val design: OrderDesignUiModel,

    val id: String,

    val quantity: String,

    val status: String,

    val totalDiscount: String,

    val totalPrice: String
)