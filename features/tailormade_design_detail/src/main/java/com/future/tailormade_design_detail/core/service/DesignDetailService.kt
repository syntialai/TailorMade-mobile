package com.future.tailormade_design_detail.core.service

import com.future.tailormade.base.model.response.BaseSingleObjectResponse
import com.future.tailormade_design_detail.core.api.DesignDetailApiUrl
import com.future.tailormade_design_detail.core.model.response.DesignDetailResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface DesignDetailService {

  @GET(DesignDetailApiUrl.DESIGNS_ID_PATH)
  fun getDesignDetailById(@Path("id") id: String): BaseSingleObjectResponse<DesignDetailResponse>
}