package com.mta.blibli.tailormade_auth.core.repository.impl

import android.app.Application
import com.future.tailormade.util.extension.flowOnIO
import com.mta.blibli.tailormade_auth.core.model.request.RefreshTokenRequest
import com.mta.blibli.tailormade_auth.core.model.request.SignInRequest
import com.mta.blibli.tailormade_auth.core.model.request.SignUpRequest
import com.mta.blibli.tailormade_auth.core.repository.AuthRepository
import com.mta.blibli.tailormade_auth.core.service.AuthService
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private var application: Application,
    private var authService: AuthService
) : AuthRepository {

    override suspend fun getFirebaseToken(userId: String) = flow {
        emit(authService.getFirebaseToken(userId))
    }.flowOnIO()

    override suspend fun refreshToken(refreshTokenRequest: RefreshTokenRequest) = flow {
        emit(authService.refreshToken(refreshTokenRequest))
    }.flowOnIO()

    override suspend fun signIn(signInRequest: SignInRequest) = flow {
        emit(authService.signIn(signInRequest))
    }.flowOnIO()

    override suspend fun signUp(signUpRequest: SignUpRequest) = flow {
        emit(authService.signUp(signUpRequest))
    }.flowOnIO()
}