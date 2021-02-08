package com.future.tailormade_design_detail.core.api

import com.future.tailormade.api.ApiUrl
import com.future.tailormade_auth.core.api.AuthApiUrl

object DesignDetailApiUrl {

  private const val DESIGNS_PATH = "/designs"

  const val UPLOAD_PATH = ApiUrl.API_PATH + "/upload/{filePath}"

  const val BASE_DESIGNS_PATH = ApiUrl.API_PATH + DESIGNS_PATH
  const val DESIGNS_ID_PATH = "$BASE_DESIGNS_PATH/{id}"

  const val TAILORS_ID_DESIGNS_PATH =  ApiUrl.API_PATH + "/tailors/{tailorId}" + DESIGNS_PATH
  const val TAILORS_ID_DESIGNS_ID_PATH = "$TAILORS_ID_DESIGNS_PATH/{id}"

  const val USERS_ID_WISHLISTS_PATH = AuthApiUrl.BASE_USERS_PATH + "/{userId}/wishlists"
}