package com.future.tailormade.base.model.response

import com.google.gson.annotations.SerializedName

data class BasePagingResponse(

    val page: Int? = null,

    @SerializedName("item_per_page")
    val itemPerPage: Int? = null,

    @SerializedName("total_page")
    val totalPage: Int? = null,

    @SerializedName("total_item")
    val totalItem: Int? = null)