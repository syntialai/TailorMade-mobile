package com.mta.blibli.tailormade_auth.core.model.request

data class SignUpRequest(

    var name: String = "",

    var email: String = "",

    var phoneNumber: String = "",

    var birthDate: String = "",

    var role: String = "",

    var gender: String = ""
)