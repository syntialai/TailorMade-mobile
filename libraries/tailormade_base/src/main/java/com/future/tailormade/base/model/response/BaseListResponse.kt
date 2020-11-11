package com.future.tailormade.base.model.response

import com.google.gson.annotations.Expose

data class BaseListResponse<T>(

    @Expose
    val data: List<T>? = null,

    @Expose
    val paging: BasePagingResponse? = null
) : BaseResponse()