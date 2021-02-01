package com.future.tailormade_profile.core.mock

import com.future.tailormade_profile.core.model.entity.Education
import com.future.tailormade_profile.core.model.entity.Location
import com.future.tailormade_profile.core.model.entity.Occupation
import com.future.tailormade_profile.core.model.response.ProfileInfoResponse

object DataMock {

  private const val USER_ID = "USER_ID"
  private const val USER_NAME = "USER_NAME"

  private val profileLocation = Location("", "", "Medan", "Sumatera Utara", "Indonesia", "")

  fun getProfileInfoMock() = ProfileInfoResponse(id = USER_ID, name = USER_NAME,
      image = "https://www.talkwalker.com/images/2020/blog-headers/image-analysis.png",
      phoneNumber = "081990333364", birthDate = 1609644391, location = profileLocation,
      education = Education(), occupation = Occupation())
}