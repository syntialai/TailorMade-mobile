package com.mta.blibli.tailormade_auth.core.repository

import com.future.tailormade.base.model.response.BaseResponse
import com.future.tailormade.base.model.response.BaseSingleObjectResponse
import com.mta.blibli.tailormade_auth.core.model.request.RefreshTokenRequest
import com.mta.blibli.tailormade_auth.core.model.request.SignInRequest
import com.mta.blibli.tailormade_auth.core.model.request.SignUpRequest
import com.mta.blibli.tailormade_auth.core.model.response.FirebaseTokenResponse
import com.mta.blibli.tailormade_auth.core.model.response.TokenResponse
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun getFirebaseToken(userId: String):
            Flow<BaseSingleObjectResponse<FirebaseTokenResponse>>

    suspend fun signIn(signInRequest: SignInRequest): Flow<BaseSingleObjectResponse<TokenResponse>>

    suspend fun signUp(signUpRequest: SignUpRequest): Flow<BaseResponse>

    suspend fun refreshToken(refreshTokenRequest: RefreshTokenRequest):
            Flow<BaseSingleObjectResponse<TokenResponse>>
}