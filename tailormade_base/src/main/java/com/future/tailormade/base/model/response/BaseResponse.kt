package com.future.tailormade.base.model.response

import com.google.gson.annotations.Expose

open class BaseResponse {

    @Expose
    var code: Int? = null

    @Expose
    var status: String? = null
}