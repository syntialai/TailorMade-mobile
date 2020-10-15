package com.mta.blibli.tailormade_auth.core.repository

import com.future.tailormade.base.model.response.BaseResponse
import com.future.tailormade.base.model.response.BaseSingleObjectResponse
import com.future.tailormade.base.model.response.BaseSingleValueResponse
import com.mta.blibli.tailormade_auth.core.model.request.LoginRequest
import com.mta.blibli.tailormade_auth.core.model.request.SignUpRequest
import com.mta.blibli.tailormade_auth.core.model.request.VerifyPhoneRequest
import com.mta.blibli.tailormade_auth.core.model.response.LoginResponse
import com.mta.blibli.tailormade_auth.core.model.response.VerifyPhoneResponse
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun getFirebaseToken(userId: String): Flow<BaseSingleValueResponse<String>>

    suspend fun verifyPhone(verifyPhoneRequest: VerifyPhoneRequest):
            Flow<BaseSingleObjectResponse<VerifyPhoneResponse>>

    suspend fun login(loginRequest: LoginRequest): Flow<BaseSingleObjectResponse<LoginResponse>>

    suspend fun signUp(signUpRequest: SignUpRequest): Flow<BaseResponse>
}