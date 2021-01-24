package com.future.tailormade_auth.core.service

import com.future.tailormade.base.model.response.BaseSingleObjectResponse
import com.future.tailormade_auth.core.api.AuthApiUrl
import com.future.tailormade_auth.core.model.request.RefreshTokenRequest
import com.future.tailormade_auth.core.model.response.ActivateTailorResponse
import com.future.tailormade_auth.core.model.response.TokenResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

  @POST(AuthApiUrl.USER_ACTIVATE_TAILOR_PATH)
  suspend fun activateTailor(): BaseSingleObjectResponse<ActivateTailorResponse>

  @POST(AuthApiUrl.USER_REFRESH_TOKEN_PATH)
  suspend fun refreshToken(
      @Body RefreshTokenRequest: RefreshTokenRequest): BaseSingleObjectResponse<TokenResponse>
}