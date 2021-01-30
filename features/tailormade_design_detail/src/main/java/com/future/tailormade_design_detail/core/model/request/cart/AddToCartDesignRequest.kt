package com.future.tailormade_design_detail.core.model.request.cart

import com.future.tailormade_design_detail.core.model.response.SizeDetailResponse

data class AddToCartDesignRequest(

    val title: String,

    val image: String,

    val price: Double,

    val discount: Double,

    val size: String,

    val color: String,

    val sizeDetail: SizeDetailResponse
)