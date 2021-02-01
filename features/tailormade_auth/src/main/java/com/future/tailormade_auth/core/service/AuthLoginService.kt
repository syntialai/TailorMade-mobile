package com.future.tailormade_auth.core.service

import com.future.tailormade.base.model.response.BaseSingleObjectResponse
import com.future.tailormade_auth.core.api.AuthApiUrl
import com.future.tailormade_auth.core.model.request.SignInRequest
import com.future.tailormade_auth.core.model.request.SignUpRequest
import com.future.tailormade_auth.core.model.response.SignInResponse
import com.future.tailormade_auth.core.model.response.UserResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthLoginService {

  @POST(AuthApiUrl.USER_SIGN_IN_PATH)
  suspend fun signIn(
      @Body SignInRequest: SignInRequest): BaseSingleObjectResponse<SignInResponse>

  @POST(AuthApiUrl.USER_SIGN_UP_PATH)
  suspend fun signUp(
      @Body SignUpRequest: SignUpRequest): BaseSingleObjectResponse<UserResponse>
}