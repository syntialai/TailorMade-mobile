package com.future.tailormade.core.model.ui.cart

data class CartUiModel(

		var id: String,

    var design: CartDesignUiModel,

    var quantity: Int = 1,
)