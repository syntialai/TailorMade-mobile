package com.future.tailormade.base.model.response

import com.google.gson.annotations.Expose

data class BaseSingleValueResponse<T>(

    @Expose
    var value: T? = null
) : BaseResponse()