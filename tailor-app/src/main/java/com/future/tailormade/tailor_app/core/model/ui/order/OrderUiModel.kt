package com.future.tailormade.tailor_app.core.model.ui.order

data class OrderUiModel(

    var id: String,

    var orderDate: String,

    var design: OrderDesignUiModel,

    var quantity: String,

    var totalDiscount: String,

    var totalPrice: String,

    var status: String
)