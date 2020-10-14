package com.mta.blibli.tailormade_auth.core.model.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

    @SerializedName("accessToken")
    var accessToken: String = "",

    @SerializedName("tokenType")
    var tokenType: String = "",

    @SerializedName("expiresIn")
    var expiresIn: Int = 0,

    @SerializedName("scope")
    var scope: String = ""
)