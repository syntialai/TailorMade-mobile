package com.future.tailormade_design_detail.core.api

import com.future.tailormade.api.ApiUrl

object DesignDetailApiUrl {

  const val BASE_DESIGNS_PATH = ApiUrl.API_PATH + "/designs"
  const val DESIGNS_ID_PATH = "$BASE_DESIGNS_PATH/{id}"
}