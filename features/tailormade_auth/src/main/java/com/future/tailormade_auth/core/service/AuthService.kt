package com.future.tailormade_auth.core.service

import com.future.tailormade.base.model.response.BaseSingleObjectResponse
import com.future.tailormade_auth.core.api.AuthApiUrl
import com.future.tailormade_auth.core.model.request.RefreshTokenRequest
import com.future.tailormade_auth.core.model.request.SignInRequest
import com.future.tailormade_auth.core.model.request.SignUpRequest
import com.future.tailormade_auth.core.model.response.ActivateTailorResponse
import com.future.tailormade_auth.core.model.response.TokenResponse
import com.future.tailormade_auth.core.model.response.UserResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

  @POST(AuthApiUrl.USERS_ACTIVATE_TAILOR_PATH)
  suspend fun activateTailor(): BaseSingleObjectResponse<ActivateTailorResponse>

  @POST(AuthApiUrl.USERS_SIGN_IN_PATH)
  suspend fun signIn(
      @Body SignInRequest: SignInRequest): BaseSingleObjectResponse<TokenResponse>

  @POST(AuthApiUrl.USERS_SIGN_UP_PATH)
  suspend fun signUp(
      @Body SignUpRequest: SignUpRequest): BaseSingleObjectResponse<UserResponse>

  @POST(AuthApiUrl.USERS_REFRESH_TOKEN_PATH)
  suspend fun refreshToken(
      @Body RefreshTokenRequest: RefreshTokenRequest): BaseSingleObjectResponse<TokenResponse>
}