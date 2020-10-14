package com.mta.blibli.tailormade_auth.core.model.request

import com.google.gson.annotations.SerializedName

data class verifyCodeRequest(

    @SerializedName("phoneNumber")
    var phoneNumber: String = ""
)