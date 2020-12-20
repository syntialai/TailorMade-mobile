package com.future.tailormade.core.model.ui.cart

data class CartDesignUiModel(

		var id: String,

    var color: String,

    var discount: String? = null,

    var image: String,

    var price: String,

    var size: String,

    var title: String)