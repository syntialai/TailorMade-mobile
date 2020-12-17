package com.future.tailormade.core.api

import com.future.tailormade.api.ApiUrl
import com.future.tailormade_auth.core.api.AuthApiUrl

object AppApiUrl {

  /**
   * Dashboard API url
   */
  private const val BASE_DASHBOARD_PATH = ApiUrl.API_PATH + "/dashboard"
  const val DASHBOARD_TAILORS_PATH = "$BASE_DASHBOARD_PATH/tailors"

  /**
   * Order API url
   */
  const val USERS_ID_ORDERS_PATH = "${AuthApiUrl.BASE_USERS_PATH}/{userId}/orders"
  const val USERS_ID_ORDERS_ID_PATH = "$USERS_ID_ORDERS_PATH/{id}"
}