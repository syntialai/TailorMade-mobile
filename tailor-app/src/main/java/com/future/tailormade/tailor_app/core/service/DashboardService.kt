package com.future.tailormade.tailor_app.core.service

import com.future.tailormade.base.model.response.BaseResponse
import com.future.tailormade.tailor_app.core.api.TailorAppApiUrl
import retrofit2.http.DELETE
import retrofit2.http.Path

interface DashboardService {

  @DELETE(TailorAppApiUrl.TAILORS_ID_DESIGNS_ID_PATH)
  suspend fun deleteDesignById(
      @Path("tailorId") tailorId: String, @Path("id") id: String): BaseResponse
}