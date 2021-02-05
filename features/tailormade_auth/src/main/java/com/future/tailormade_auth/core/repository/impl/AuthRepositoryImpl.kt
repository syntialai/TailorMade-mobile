package com.future.tailormade_auth.core.repository.impl

import com.future.tailormade.base.model.response.BaseSingleObjectResponse
import com.future.tailormade.util.extension.flowOnIO
import com.future.tailormade_auth.core.model.request.RefreshTokenRequest
import com.future.tailormade_auth.core.model.request.SignInRequest
import com.future.tailormade_auth.core.model.request.SignUpRequest
import com.future.tailormade_auth.core.model.response.SignInResponse
import com.future.tailormade_auth.core.model.response.TokenDetailResponse
import com.future.tailormade_auth.core.model.response.TokenResponse
import com.future.tailormade_auth.core.repository.AuthRepository
import com.future.tailormade_auth.core.service.AuthLoginService
import com.future.tailormade_auth.core.service.AuthService
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AuthRepositoryImpl @Inject constructor(private val authService: AuthService,
    private val authLoginService: AuthLoginService) :
    AuthRepository {

  override suspend fun refreshToken(
      refreshTokenRequest: RefreshTokenRequest): Flow<TokenDetailResponse> = flow {
    authService.refreshToken(refreshTokenRequest).data?.token?.let {
      emit(it)
    }
//    emit(getTokenResponse())
  }.flowOnIO()

  override suspend fun signIn(signInRequest: SignInRequest): Flow<SignInResponse> = flow {
    authLoginService.signIn(signInRequest).data?.let {
      emit(it)
    }
  }.flowOnIO()

  override suspend fun signUp(signUpRequest: SignUpRequest) = flow {
    authLoginService.signUp(signUpRequest).data?.let {
      emit(it)
    }
  }.flowOnIO()

  private fun getTokenResponse() = BaseSingleObjectResponse(
      TokenResponse(TokenDetailResponse("Dummy access token", "Dummy refresh token")))
}