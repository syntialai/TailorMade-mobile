package com.future.tailormade_auth.core.service

import com.future.tailormade.base.model.response.BaseSingleObjectResponse
import com.future.tailormade_auth.core.api.AuthApiUrl
import com.future.tailormade_auth.core.model.request.RefreshTokenRequest
import com.future.tailormade_auth.core.model.response.ActivateTailorResponse
import com.future.tailormade_auth.core.model.response.TokenResponse
import com.future.tailormade_auth.core.model.response.UserResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthService {

  @GET(AuthApiUrl.USER_GET_USER_INFO)
  suspend fun getUserInfo(): BaseSingleObjectResponse<UserResponse>

  @POST(AuthApiUrl.USER_ACTIVATE_TAILOR_PATH)
  suspend fun activateTailor(): BaseSingleObjectResponse<ActivateTailorResponse>

  @POST(AuthApiUrl.USER_REFRESH_TOKEN_PATH)
  suspend fun refreshToken(
      @Body RefreshTokenRequest: RefreshTokenRequest): BaseSingleObjectResponse<TokenResponse>
}