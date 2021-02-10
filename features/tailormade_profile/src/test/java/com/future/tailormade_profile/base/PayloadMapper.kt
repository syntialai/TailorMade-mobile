package com.future.tailormade_profile.base

import com.future.tailormade.base.test.BaseTest
import com.future.tailormade_auth.core.model.request.SignInRequest
import com.future.tailormade_auth.core.model.request.SignUpRequest
import com.future.tailormade_auth.core.model.response.SignInResponse
import com.future.tailormade_auth.core.model.response.TokenDetailResponse
import com.future.tailormade_auth.core.model.response.UserResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
object PayloadMapper {

  fun getSignInRequest() = SignInRequest(username = BaseTest.USER_EMAIL,
      password = BaseTest.USER_PASSWORD, role = BaseTest.USER_ROLE)

  fun getSignUpRequest() = SignUpRequest(name = BaseTest.USER_NAME, email = BaseTest.USER_EMAIL,
      password = BaseTest.USER_PASSWORD, birthDate = BaseTest.USER_BIRTHDATE,
      gender = BaseTest.USER_GENDER, role = BaseTest.USER_ROLE)

  fun getUserResponse() = UserResponse(id = BaseTest.USER_ID, name = BaseTest.USER_NAME,
      email = BaseTest.USER_EMAIL, gender = BaseTest.USER_GENDER.name,
      role = BaseTest.USER_ROLE.name)
}