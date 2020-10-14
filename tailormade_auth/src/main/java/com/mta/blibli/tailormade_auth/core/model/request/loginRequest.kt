package com.mta.blibli.tailormade_auth.core.model.request

import com.google.gson.annotations.SerializedName

data class loginRequest(

    @SerializedName("phoneNumber")
    var phoneNumber: String = "",

    @SerializedName("verificationCode")
    var verificationCode: String = ""
)