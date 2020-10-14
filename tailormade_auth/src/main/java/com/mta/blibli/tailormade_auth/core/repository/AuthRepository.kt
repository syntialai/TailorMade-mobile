package com.mta.blibli.tailormade_auth.core.repository

import android.app.Application
import com.future.tailormade.util.extension.flowOnIO
import com.mta.blibli.tailormade_auth.core.model.request.LoginRequest
import com.mta.blibli.tailormade_auth.core.model.request.SignUpRequest
import com.mta.blibli.tailormade_auth.core.model.request.VerifyPhoneRequest
import com.mta.blibli.tailormade_auth.core.service.AuthService
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private var application: Application,
    private var authService: AuthService
) {

    suspend fun getFirebaseToken(userId: String) = flow {
        emit(authService.getFirebaseToken(userId))
    }.flowOnIO()

    suspend fun verifyPhone(verifyPhoneRequest: VerifyPhoneRequest) = flow {
        emit(authService.verifyPhone(verifyPhoneRequest))
    }.flowOnIO()

    suspend fun login(loginRequest: LoginRequest) = flow {
        emit(authService.login(loginRequest))
    }.flowOnIO()

    suspend fun signUp(signUpRequest: SignUpRequest) = flow {
        emit(authService.signUp(signUpRequest))
    }.flowOnIO()
}