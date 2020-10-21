package com.future.tailormade.base.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.sql.Timestamp

open class BaseResponse {

    @SerializedName("timestamp")
    @Expose
    var timestamp: Timestamp? = null

    @SerializedName("errorCode")
    @Expose
    var errorCode: Int? = null

    @SerializedName("errorMessage")
    @Expose
    var errorMessage: String? = null

    @SerializedName("success")
    @Expose
    var success: Boolean? = null
}