package com.future.tailormade.base.model.response

data class BasePagingResponse(

    val page: Int? = null,

    val itemPerPage: Int? = null,

    val totalPage: Int? = null
)