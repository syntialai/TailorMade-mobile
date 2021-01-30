package com.future.tailormade_design_detail.core.model.request.cart

import com.future.tailormade_design_detail.core.model.response.SizeDetailResponse

data class AddToCartDesignRequest(

    val id: String,

    val title: String,

    val image: String,

    val price: Double,

    val discount: Double,

    var size: String,

    var color: String,

    var sizeDetail: SizeDetailResponse? = null
)