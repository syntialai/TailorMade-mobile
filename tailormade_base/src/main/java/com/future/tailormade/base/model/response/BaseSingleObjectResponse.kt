package com.future.tailormade.base.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BaseSingleObjectResponse<T>(

    @SerializedName("content")
    @Expose
    var content: T? = null
) : BaseResponse()