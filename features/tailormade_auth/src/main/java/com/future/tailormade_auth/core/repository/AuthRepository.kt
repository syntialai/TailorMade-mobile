package com.future.tailormade_auth.core.repository

import com.future.tailormade_auth.core.model.request.RefreshTokenRequest
import com.future.tailormade_auth.core.model.request.SignInRequest
import com.future.tailormade_auth.core.model.request.SignUpRequest
import com.future.tailormade_auth.core.model.response.ActivateTailorResponse
import com.future.tailormade_auth.core.model.response.SignInResponse
import com.future.tailormade_auth.core.model.response.TokenDetailResponse
import com.future.tailormade_auth.core.model.response.UserResponse
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

  suspend fun signIn(signInRequest: SignInRequest): Flow<SignInResponse>

  suspend fun signUp(signUpRequest: SignUpRequest): Flow<UserResponse>

  suspend fun refreshToken(refreshTokenRequest: RefreshTokenRequest): Flow<TokenDetailResponse>

  suspend fun activateTailor(): Flow<ActivateTailorResponse>
}