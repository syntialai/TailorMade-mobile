package com.mta.blibli.tailormade_auth.core.service

import com.future.tailormade.base.model.response.BaseResponse
import com.future.tailormade.base.model.response.BaseSingleObjectResponse
import com.future.tailormade.base.model.response.BaseSingleValueResponse
import com.mta.blibli.tailormade_auth.core.api.AuthApiUrl
import com.mta.blibli.tailormade_auth.core.model.request.LoginRequest
import com.mta.blibli.tailormade_auth.core.model.request.SignUpRequest
import com.mta.blibli.tailormade_auth.core.model.request.VerifyPhoneRequest
import com.mta.blibli.tailormade_auth.core.model.response.LoginResponse
import com.mta.blibli.tailormade_auth.core.model.response.VerifyPhoneResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthService {

    @GET(AuthApiUrl.API_GET_FIREBASE_TOKEN_PATH)
    fun getFirebaseToken(@Query("userId") userId: String): BaseSingleValueResponse<String>

    @POST(AuthApiUrl.API_LOGIN_PATH)
    fun login(@Body LoginRequest: LoginRequest): BaseSingleObjectResponse<LoginResponse>

    @POST(AuthApiUrl.API_SIGN_UP_PATH)
    fun signUp(@Body SignUpRequest: SignUpRequest): BaseResponse

    @POST(AuthApiUrl.API_VERIFY_PHONE_PATH)
    fun verifyPhone(@Body VerifyPhoneRequest: VerifyPhoneRequest): BaseSingleObjectResponse<VerifyPhoneResponse>
}