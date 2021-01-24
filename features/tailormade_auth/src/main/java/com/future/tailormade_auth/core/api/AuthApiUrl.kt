package com.future.tailormade_auth.core.api

import com.future.tailormade.api.ApiUrl

object AuthApiUrl {

  private const val BASE_USER_PATH = ApiUrl.API_PATH + "/user"
  const val BASE_USERS_PATH = ApiUrl.API_PATH + "/users"

  const val USER_ACTIVATE_TAILOR_PATH = "$BASE_USER_PATH/_activate-tailor"
  const val USER_SIGN_IN_PATH = "$BASE_USER_PATH/_sign-in"
  const val USER_SIGN_UP_PATH = "$BASE_USER_PATH/_sign-up"
  const val USER_REFRESH_TOKEN_PATH = "$BASE_USER_PATH/_refresh-token"
  const val USER_GET_FIREBASE_TOKEN_PATH = "$BASE_USER_PATH/_get-firebase-token"
}