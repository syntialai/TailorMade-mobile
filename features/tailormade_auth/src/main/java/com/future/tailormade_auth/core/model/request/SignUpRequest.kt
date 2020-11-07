package com.future.tailormade_auth.core.model.request

data class SignUpRequest(

    var name: String = "",

    var email: String = "",

    var password: String = "",

    var birthDate: String = "",

    var gender: String = "")