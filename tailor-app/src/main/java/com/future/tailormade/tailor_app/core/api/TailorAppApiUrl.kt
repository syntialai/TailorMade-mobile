package com.future.tailormade.tailor_app.core.api

import com.future.tailormade_profile.core.api.ProfileApiUrl

object TailorAppApiUrl {

  /**
   * Dashboard API Path
   */
  const val TAILORS_ID_DESIGNS_ID_PATH = ProfileApiUrl.TAILORS_ID_DESIGNS_PATH + "/{id}"

  /**
   * Orders API Path
   */
  const val BASE_TAILORS_ID_ORDERS_PATH = ProfileApiUrl.TAILORS_ID_PATH + "/orders"
  const val TAILORS_ID_ORDERS_ID_PATH = "$BASE_TAILORS_ID_ORDERS_PATH/{id}"
}