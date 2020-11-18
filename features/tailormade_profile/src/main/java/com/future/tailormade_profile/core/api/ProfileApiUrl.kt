package com.future.tailormade_profile.core.api

import com.future.tailormade_auth.core.api.AuthApiUrl

object ProfileApiUrl {

  const val BASE_NOMINATIM_API_PATH = "https://nominatim.openstreetmap.org/search/"
  const val NOMINATIM_SEARCH_BY_QUERY_PATH = "{query}"

  const val USERS_ID_PATH = AuthApiUrl.BASE_USERS_PATH + "/{id}"
  const val USERS_ID_UPDATE_BASIC_INFO_PATH = "$USERS_ID_PATH/_update-basic-info"
  const val USERS_ID_UPDATE_MORE_INFO_PATH = "$USERS_ID_PATH/_update-more-info"
}