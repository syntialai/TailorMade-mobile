package com.future.tailormade_auth.core.repository.impl

import com.future.tailormade_auth.core.model.request.RefreshTokenRequest
import com.future.tailormade_auth.core.model.request.SignInRequest
import com.future.tailormade_auth.core.model.request.SignUpRequest
import com.future.tailormade_auth.core.model.response.SignInResponse
import com.future.tailormade_auth.core.model.response.TokenDetailResponse
import com.future.tailormade_auth.core.repository.AuthRepository
import com.future.tailormade_auth.core.service.AuthLoginService
import com.future.tailormade_auth.core.service.AuthService
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class AuthRepositoryImpl @Inject constructor(private val authService: AuthService,
    private val authLoginService: AuthLoginService, private val ioDispatcher: CoroutineDispatcher) :
    AuthRepository {

  override suspend fun refreshToken(
      refreshTokenRequest: RefreshTokenRequest): Flow<TokenDetailResponse> = flow {
    authService.refreshToken(refreshTokenRequest).data?.token?.let {
      emit(it)
    }
  }.flowOn(ioDispatcher)

  override suspend fun signIn(signInRequest: SignInRequest): Flow<SignInResponse> = flow {
    authLoginService.signIn(signInRequest).data?.let {
      emit(it)
    }
  }.flowOn(ioDispatcher)

  override suspend fun signUp(signUpRequest: SignUpRequest) = flow {
    authLoginService.signUp(signUpRequest).data?.let {
      emit(it)
    }
  }.flowOn(ioDispatcher)
}