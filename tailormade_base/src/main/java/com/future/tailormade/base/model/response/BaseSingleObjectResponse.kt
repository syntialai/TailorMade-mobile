package com.future.tailormade.base.model.response

import com.google.gson.annotations.Expose

data class BaseSingleObjectResponse<T>(

    @Expose
    var data: T? = null
) : BaseResponse()