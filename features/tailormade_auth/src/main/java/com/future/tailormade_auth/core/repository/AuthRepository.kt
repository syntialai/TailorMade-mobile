package com.future.tailormade_auth.core.repository

import com.future.tailormade.base.model.response.BaseSingleObjectResponse
import com.future.tailormade_auth.core.model.request.RefreshTokenRequest
import com.future.tailormade_auth.core.model.request.SignInRequest
import com.future.tailormade_auth.core.model.request.SignUpRequest
import com.future.tailormade_auth.core.model.response.FirebaseTokenResponse
import com.future.tailormade_auth.core.model.response.TokenResponse
import com.future.tailormade_auth.core.model.response.UserResponse
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

  suspend fun getFirebaseToken(userId: String): Flow<BaseSingleObjectResponse<FirebaseTokenResponse>>

  suspend fun signIn(signInRequest: SignInRequest): Flow<BaseSingleObjectResponse<TokenResponse>>

  suspend fun signUp(signUpRequest: SignUpRequest): Flow<BaseSingleObjectResponse<UserResponse>>

  suspend fun refreshToken(refreshTokenRequest: RefreshTokenRequest): Flow<BaseSingleObjectResponse<TokenResponse>>

  suspend fun setRole(userId: String, role: String): Flow<BaseSingleObjectResponse<UserResponse>>
}