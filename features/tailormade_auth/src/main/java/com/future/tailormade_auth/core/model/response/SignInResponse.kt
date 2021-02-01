package com.future.tailormade_auth.core.model.response

data class SignInResponse(

  val token: TokenDetailResponse,

  val user: UserResponse
)