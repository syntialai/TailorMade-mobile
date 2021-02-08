package com.future.tailormade_profile.core.api

import com.future.tailormade.api.ApiUrl
import com.future.tailormade_auth.core.api.AuthApiUrl

object ProfileApiUrl {

  const val BASE_NOMINATIM_API_PATH = "https://nominatim.openstreetmap.org/"
  const val NOMINATIM_SEARCH_BY_QUERY_PATH = "search/{query}"

  const val USERS_ID_PATH = AuthApiUrl.BASE_USERS_PATH + "/{id}"
  const val USERS_ID_UPDATE_BASIC_INFO_PATH = "$USERS_ID_PATH/_update-basic-info"
  const val USERS_ID_UPDATE_MORE_INFO_PATH = "$USERS_ID_PATH/_update-more-info"

  const val BASE_TAILORS_PATH = ApiUrl.API_PATH + "/tailors"
  const val TAILORS_ID_PATH = "$BASE_TAILORS_PATH/{tailorId}"
  const val TAILORS_ID_DESIGNS_PATH = "$TAILORS_ID_PATH/designs"
}