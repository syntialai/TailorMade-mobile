package com.mta.blibli.tailormade_auth.core.model.request

data class LoginRequest(

    var phoneNumber: String = "",

    var verificationCode: String = ""
)