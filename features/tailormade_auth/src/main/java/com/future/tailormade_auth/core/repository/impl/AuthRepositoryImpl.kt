package com.future.tailormade_auth.core.repository.impl

import com.future.tailormade.base.model.response.BaseSingleObjectResponse
import com.future.tailormade.util.extension.flowOnIO
import com.future.tailormade_auth.core.model.request.RefreshTokenRequest
import com.future.tailormade_auth.core.model.request.SignInRequest
import com.future.tailormade_auth.core.model.request.SignUpRequest
import com.future.tailormade_auth.core.model.response.ActivateTailorResponse
import com.future.tailormade_auth.core.model.response.TokenDetailResponse
import com.future.tailormade_auth.core.model.response.TokenResponse
import com.future.tailormade_auth.core.repository.AuthRepository
import com.future.tailormade_auth.core.service.AuthService
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AuthRepositoryImpl @Inject constructor(private var authService: AuthService) :
    AuthRepository {

  override suspend fun refreshToken(refreshTokenRequest: RefreshTokenRequest) = flow {
//    emit(authService.refreshToken(refreshTokenRequest))
    emit(getTokenResponse())
  }.flowOnIO()

  override suspend fun activateTailor(): Flow<BaseSingleObjectResponse<ActivateTailorResponse>> = flow {
    emit(authService.activateTailor())
  }.flowOnIO()

  override suspend fun signIn(signInRequest: SignInRequest) = flow {
    emit(authService.signIn(signInRequest))
  }.flowOnIO()

  override suspend fun signUp(signUpRequest: SignUpRequest) = flow {
    emit(authService.signUp(signUpRequest))
  }.flowOnIO()

  private fun getTokenResponse() = BaseSingleObjectResponse(
      TokenResponse(TokenDetailResponse("Dummy access token", "Dummy refresh token")))
}