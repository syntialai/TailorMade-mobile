package com.future.tailormade_auth.core.model.request

data class SignUpRequest(

    val name: String,

    val email: String,

    val password: String,

    val birthDate: String,

    val gender: String = ""
)