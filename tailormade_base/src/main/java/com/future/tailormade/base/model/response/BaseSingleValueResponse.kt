package com.future.tailormade.base.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BaseSingleValueResponse<T>(

    @SerializedName("value")
    @Expose
    var value: T? = null
) : BaseResponse()