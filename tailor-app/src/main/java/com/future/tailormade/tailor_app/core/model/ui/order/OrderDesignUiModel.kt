package com.future.tailormade.tailor_app.core.model.ui.order

data class OrderDesignUiModel(

    var color: String,

    var discount: String? = null,

    var id: String,

    var image: String,

    var price: String,

    var size: String,

    var title: String
)