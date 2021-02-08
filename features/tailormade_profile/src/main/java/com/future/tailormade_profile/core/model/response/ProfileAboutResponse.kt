package com.future.tailormade_profile.core.model.response

import com.future.tailormade_profile.core.model.entity.Education
import com.future.tailormade_profile.core.model.entity.Occupation

data class ProfileAboutResponse (

  val occupation: Occupation? = null,

  val education: Education? = null
)