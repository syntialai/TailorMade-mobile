package com.future.tailormade.base.model.response

import com.google.gson.annotations.Expose

data class BaseSingleObjectResponse<T>(

    @Expose
    var content: T? = null
) : BaseResponse()