package com.future.tailormade_profile.core.mapper

import com.future.tailormade_profile.core.model.entity.Location
import com.future.tailormade_profile.core.model.response.ProfileInfoResponse
import com.future.tailormade_profile.core.model.ui.ProfileInfoUiModel

object ProfileMapper {

  fun mapToProfileInfoUiModel(profileInfo: ProfileInfoResponse) = ProfileInfoUiModel(
      id = profileInfo.id, name = profileInfo.name, image = profileInfo.image,
      phoneNumber = profileInfo.phoneNumber, birthDate = profileInfo.birthDate,
      address = getAddress(profileInfo.location), location = profileInfo.location,
      occupation = profileInfo.occupation, education = profileInfo.education)

  private fun getAddress(location: Location?) = location?.city.orEmpty() + if (location?.province.isNullOrBlank().not()) {
    ", ${location?.province}"
  } else {
    ""
  }
}