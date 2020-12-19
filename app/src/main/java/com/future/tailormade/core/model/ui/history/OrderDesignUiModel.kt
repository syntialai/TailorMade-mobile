package com.future.tailormade.core.model.ui.history

import com.google.gson.annotations.Expose

data class OrderDesignUiModel(

    var color: String,

    var discount: String? = null,

    var id: String,

    var image: String,

    var price: String,

    var size: String,

		var title: String
)