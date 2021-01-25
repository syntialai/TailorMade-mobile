package com.future.tailormade_auth.core.model.response

import android.os.Parcelable
import com.future.tailormade.base.model.enums.GenderEnum
import com.future.tailormade.base.model.enums.RoleEnum
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserResponse (

  val id: String = "",

  val name: String = "",

  val email: String = "",

  val birthDate: String = "",

  val gender: GenderEnum = GenderEnum.Anonymous,

  val role: RoleEnum = RoleEnum.ROLE_USER
): Parcelable