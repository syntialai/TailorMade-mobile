package com.mta.blibli.tailormade_auth.core.model.request

import com.google.gson.annotations.SerializedName

data class SignUpRequest(

    @SerializedName("name")
    var name: String = "",

    @SerializedName("email")
    var email: String = "",

    @SerializedName("phoneNumber")
    var phoneNumber: String = "",

    @SerializedName("birthDate")
    var birthDate: String = ""
)