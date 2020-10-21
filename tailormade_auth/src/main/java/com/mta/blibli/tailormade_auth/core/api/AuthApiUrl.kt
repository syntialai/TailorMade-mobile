package com.mta.blibli.tailormade_auth.core.api

import com.future.tailormade.api.ApiUrl

object AuthApiUrl {

    const val API_LOGIN_PATH = ApiUrl.API_PATH + "/login"
    const val API_SIGN_UP_PATH = ApiUrl.API_PATH + "/sign-up"
    const val API_VERIFY_PHONE_PATH = ApiUrl.API_PATH + "/verify-phone"
    const val API_GET_FIREBASE_TOKEN_PATH = ApiUrl.API_PATH + "/3p/token"
}