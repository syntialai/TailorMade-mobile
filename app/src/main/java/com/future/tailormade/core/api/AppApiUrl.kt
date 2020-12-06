package com.future.tailormade.core.api

import com.future.tailormade.api.ApiUrl

object AppApiUrl {

  /**
   * Dashboard API url
   */
  private const val BASE_DASHBOARD_PATH = ApiUrl.API_PATH + "/dashboard"
  const val DASHBOARD_TAILORS = "$BASE_DASHBOARD_PATH/tailors"
}