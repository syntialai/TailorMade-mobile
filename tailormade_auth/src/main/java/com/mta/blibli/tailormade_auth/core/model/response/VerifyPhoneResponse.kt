package com.mta.blibli.tailormade_auth.core.model.response

import com.google.gson.annotations.SerializedName

data class VerifyPhoneResponse(

    @SerializedName("verificationCode")
    var verificationCode: String = "",

    @SerializedName("expiresIn")
    var expiresIn: Int = 0
)