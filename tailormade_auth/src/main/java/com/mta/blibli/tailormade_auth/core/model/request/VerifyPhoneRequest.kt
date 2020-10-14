package com.mta.blibli.tailormade_auth.core.model.request

import com.google.gson.annotations.SerializedName

data class VerifyPhoneRequest(

    @SerializedName("phoneNumber")
    var phoneNumber: String = ""
)