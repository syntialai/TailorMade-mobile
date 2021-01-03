package com.future.tailormade.core.model.response.cart

import com.google.gson.annotations.Expose

data class CartDesignResponse(

    val color: String,

    val discount: Double,

    val id: String,

    val image: String,

    val price: Double,

    val size: String,

    @Expose
    val sizeDetail: CartSizeDetailResponse? = null,

    val title: String) {

    fun getMockResponse() = this.copy(id = "DESIGN_1",
        image = "https://www.talkwalker.com/images/2020/blog-headers/image-analysis.png",
        color = "Blue", discount = 500.0, price = 50000.0, size = "S",
        sizeDetail = CartSizeDetailResponse().getMockResponse(), title = "Design 1")
}