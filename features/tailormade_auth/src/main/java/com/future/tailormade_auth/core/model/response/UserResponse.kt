package com.future.tailormade_auth.core.model.response

data class UserResponse (

  val id: String = "",

  val name: String = "",

  val email: String = "",

  val birthDate: String = "",

  val gender: String = "",

  val role: Int? = null
)