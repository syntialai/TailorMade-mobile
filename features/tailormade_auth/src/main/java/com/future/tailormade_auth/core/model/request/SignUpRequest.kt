package com.future.tailormade_auth.core.model.request

import com.future.tailormade.base.model.enums.GenderEnum
import com.future.tailormade.base.model.enums.RoleEnum

data class SignUpRequest(

    val name: String,

    val email: String,

    val password: String,

    val birthDate: String,

    val gender: GenderEnum = GenderEnum.Anonymous,

    val role: RoleEnum = RoleEnum.ROLE_USER
)