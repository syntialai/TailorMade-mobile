package com.mta.blibli.tailormade_auth.core.model.response

data class VerifyPhoneResponse(

    var verificationCode: String = "",

    var expiresIn: Int = 0
)