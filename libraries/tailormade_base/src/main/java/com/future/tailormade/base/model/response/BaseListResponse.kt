package com.future.tailormade.base.model.response

import com.google.gson.annotations.Expose

data class BaseListResponse<T>(

    @Expose
    var data: List<T>? = null
) : BaseResponse()