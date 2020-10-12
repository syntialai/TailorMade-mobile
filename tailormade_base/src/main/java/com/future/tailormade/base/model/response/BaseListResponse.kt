package com.future.tailormade.base.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BaseListResponse<T>(

    @SerializedName("content")
    @Expose
    var content: List<T>? = null
) : BaseResponse()