package com.future.tailormade_auth.core.model.request

import com.future.tailormade.base.model.enums.RoleEnum

data class SignInRequest(

    val username: String,

    val password: String,

    val role: RoleEnum
)