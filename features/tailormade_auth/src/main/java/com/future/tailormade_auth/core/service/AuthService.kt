package com.future.tailormade_auth.core.service

import com.future.tailormade.base.model.response.BaseSingleObjectResponse
import com.future.tailormade_auth.core.api.AuthApiUrl
import com.future.tailormade_auth.core.model.request.RefreshTokenRequest
import com.future.tailormade_auth.core.model.request.SignInRequest
import com.future.tailormade_auth.core.model.request.SignUpRequest
import com.future.tailormade_auth.core.model.response.FirebaseTokenResponse
import com.future.tailormade_auth.core.model.response.TokenResponse
import com.future.tailormade_auth.core.model.response.UserResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AuthService {

  @GET(AuthApiUrl.USERS_GET_FIREBASE_TOKEN_PATH)
  fun getFirebaseToken(
    @Path("userId") userId: String
  ): BaseSingleObjectResponse<FirebaseTokenResponse>

  @POST(AuthApiUrl.USERS_SET_ROLE_PATH)
  fun setRole(
    @Path("id") userId: String,
    @Body role: String
  ): BaseSingleObjectResponse<UserResponse>

  @POST(AuthApiUrl.USERS_SIGN_IN_PATH)
  fun signIn(
    @Body SignInRequest: SignInRequest
  ): BaseSingleObjectResponse<TokenResponse>

  @POST(AuthApiUrl.USERS_SIGN_UP_PATH)
  fun signUp(
    @Body SignUpRequest: SignUpRequest
  ): BaseSingleObjectResponse<UserResponse>

  @POST(AuthApiUrl.USERS_REFRESH_TOKEN_PATH)
  fun refreshToken(
    @Body RefreshTokenRequest: RefreshTokenRequest
  ): BaseSingleObjectResponse<TokenResponse>
}