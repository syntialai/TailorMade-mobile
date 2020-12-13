package com.future.tailormade.core.model.ui.cart

data class CartDesignUiModel(

    var color: String,

    var discount: String? = null,

    var id: String,

    var image: String,

    var price: String,

    var size: String,

    var title: String
)