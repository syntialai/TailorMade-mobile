package com.future.tailormade_profile.core.api

import com.future.tailormade_auth.core.api.AuthApiUrl

object ProfileApiUrl {

  const val NOMINATIM_API = "https://nominatim.openstreetmap.org/search/"
  const val USERS_ID_PATH = AuthApiUrl.BASE_USERS_PATH + "/{id}"
}