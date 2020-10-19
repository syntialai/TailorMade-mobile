package com.future.tailormade.base.model.response

import com.google.gson.annotations.Expose
import java.sql.Timestamp

open class BaseResponse {

    @Expose
    var timestamp: Timestamp? = null

    @Expose
    var errorCode: Int? = null

    @Expose
    var errorMessage: String? = null

    @Expose
    var success: Boolean? = null
}