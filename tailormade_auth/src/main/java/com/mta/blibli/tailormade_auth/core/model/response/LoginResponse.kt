package com.mta.blibli.tailormade_auth.core.model.response

data class LoginResponse(

    var accessToken: String = "",

    var tokenType: String = "",

    var expiresIn: Int = 0,

    var scope: String = ""
)