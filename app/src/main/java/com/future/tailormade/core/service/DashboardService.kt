package com.future.tailormade.core.service

import com.future.tailormade.base.model.response.BaseListResponse
import com.future.tailormade.core.api.AppApiUrl
import com.future.tailormade.core.model.response.dashboard.DashboardTailorResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface DashboardService {

  @GET(AppApiUrl.DASHBOARD_TAILORS)
  fun getDashboardTailors(@Query("lat") lat: Double, @Query("lon") lon: Double,
      @Query("page") page: Int, @Query("itemPerPage") itemPerPage: Int):
      BaseListResponse<DashboardTailorResponse>
}