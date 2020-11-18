package com.future.tailormade_profile.core.api

import com.future.tailormade_auth.core.api.AuthApiUrl

object ProfileApiUrl {

  const val NOMINATIM_API = "https://nominatim.openstreetmap.org/search/{query}"

  const val USERS_ID_PATH = AuthApiUrl.BASE_USERS_PATH + "/{id}"
  const val USERS_ID_UPDATE_BASIC_INFO_PATH = "$USERS_ID_PATH/_update-basic-info"
  const val USERS_ID_UPDATE_MORE_INFO_PATH = "$USERS_ID_PATH/_update-more-info"
}