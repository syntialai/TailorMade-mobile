package com.future.tailormade_auth.core.api

import com.future.tailormade.api.ApiUrl

object AuthApiUrl {

  const val BASE_USERS_PATH = ApiUrl.API_PATH + "/users"

  const val USERS_ACTIVATE_TAILOR_PATH = "$BASE_USERS_PATH/_activate-tailor"
  const val USERS_SIGN_IN_PATH = "$BASE_USERS_PATH/_sign-in"
  const val USERS_SIGN_UP_PATH = "$BASE_USERS_PATH/_sign-up"
  const val USERS_REFRESH_TOKEN_PATH = "$BASE_USERS_PATH/_refresh-token"
  const val USERS_GET_FIREBASE_TOKEN_PATH = "$BASE_USERS_PATH/_get-firebase-token"
}