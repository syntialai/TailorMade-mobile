package com.future.tailormade_profile.base

import com.future.tailormade.base.test.BaseTest
import com.future.tailormade_profile.core.model.entity.Education
import com.future.tailormade_profile.core.model.entity.Location
import com.future.tailormade_profile.core.model.entity.Occupation
import com.future.tailormade_profile.core.model.request.UpdateProfileAboutRequest
import com.future.tailormade_profile.core.model.request.UpdateProfileRequest
import com.future.tailormade_profile.core.model.response.AddressResponse
import com.future.tailormade_profile.core.model.response.LocationResponse
import com.future.tailormade_profile.core.model.response.ProfileAboutResponse
import com.future.tailormade_profile.core.model.response.ProfileDesignResponse
import com.future.tailormade_profile.core.model.response.ProfileInfoResponse
import com.future.tailormade_profile.core.model.ui.ProfileInfoUiModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
object PayloadMapper {

  fun getLocationResponses() = arrayListOf(
      LocationResponse(lat = "2.9", lon = "99.8", display_name = "DISPLAY NAME",
          address = AddressResponse(city_district = "DISTRICT", city = "CITY", state = "STATE",
              country = "COUNTRY", postcode = "POST CODE")))

  fun getMappedLocationResponses() = arrayListOf("DISPLAY NAME")

  fun getProfileAboutResponse() = ProfileAboutResponse(occupation = Occupation(),
      education = Education())

  fun getProfileDesignResponses() = listOf(
      ProfileDesignResponse(id = BaseTest.DESIGN_ID, title = BaseTest.DESIGN_TITLE,
          image = BaseTest.DESIGN_IMAGE, price = BaseTest.DESIGN_PRICE,
          discount = BaseTest.DESIGN_DISCOUNT, active = true))

  fun getProfileLocation() = Location(2.55f, 2.56f, "Medan", "Sumatera Utara", "Indonesia", "",
      "", "")

  fun getProfileInfoResponse() = ProfileInfoResponse(id = BaseTest.USER_ID,
      name = BaseTest.USER_NAME, image = null, phoneNumber = null,
      birthDate = BaseTest.USER_BIRTHDATE, location = null, education = Education(),
      occupation = Occupation())

  fun getProfileInfoUiModel() = ProfileInfoUiModel(id = BaseTest.USER_ID, name = BaseTest.USER_NAME,
      image = null, phoneNumber = null, birthDate = BaseTest.USER_BIRTHDATE, address = "",
      location = null, education = Education(), occupation = Occupation())

  fun getTailorProfileInfoResponse() = ProfileInfoResponse(id = BaseTest.TAILOR_ID,
      name = BaseTest.TAILOR_NAME, image = null, phoneNumber = null,
      birthDate = BaseTest.USER_BIRTHDATE, location = null, education = Education(),
      occupation = Occupation())

  fun getTailorProfileInfoUiModel() = ProfileInfoUiModel(id = BaseTest.TAILOR_ID,
      name = BaseTest.TAILOR_NAME, image = null, phoneNumber = null,
      birthDate = BaseTest.USER_BIRTHDATE, address = "", location = null, education = Education(),
      occupation = Occupation())

  fun getUpdateProfileAboutRequest() = UpdateProfileAboutRequest(education = Education(),
      occupation = Occupation())

  fun getUpdateProfileRequest() = UpdateProfileRequest(name = BaseTest.USER_NAME,
      birthDate = BaseTest.USER_BIRTHDATE, phoneNumber = "")
}