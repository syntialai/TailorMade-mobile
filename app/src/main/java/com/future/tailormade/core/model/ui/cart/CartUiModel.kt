package com.future.tailormade.core.model.ui.cart

data class CartUiModel(

    var design: CartDesignUiModel,

    var id: String,

    var quantity: Int = 1,

    var userId: String
)