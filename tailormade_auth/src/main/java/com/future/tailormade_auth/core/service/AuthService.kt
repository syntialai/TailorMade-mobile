package com.future.tailormade_auth.core.service

import com.future.tailormade.base.model.response.BaseResponse
import com.future.tailormade.base.model.response.BaseSingleObjectResponse
import com.future.tailormade_auth.core.api.AuthApiUrl
import com.future.tailormade_auth.core.model.request.RefreshTokenRequest
import com.future.tailormade_auth.core.model.request.SignInRequest
import com.future.tailormade_auth.core.model.request.SignUpRequest
import com.future.tailormade_auth.core.model.response.FirebaseTokenResponse
import com.future.tailormade_auth.core.model.response.TokenResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthService {

    @GET(AuthApiUrl.USERS_GET_FIREBASE_TOKEN_PATH)
    fun getFirebaseToken(@Query("userId") userId: String): BaseSingleObjectResponse<FirebaseTokenResponse>

    @POST(AuthApiUrl.USERS_SIGN_IN_PATH)
    fun signIn(@Body SignInRequest: SignInRequest): BaseSingleObjectResponse<TokenResponse>

    @POST(AuthApiUrl.USERS_SIGN_UP_PATH)
    fun signUp(@Body SignUpRequest: SignUpRequest): BaseResponse

    @POST(AuthApiUrl.USERS_REFRESH_TOKEN_PATH)
    fun refreshToken(@Body RefreshTokenRequest: RefreshTokenRequest): BaseSingleObjectResponse<TokenResponse>
}