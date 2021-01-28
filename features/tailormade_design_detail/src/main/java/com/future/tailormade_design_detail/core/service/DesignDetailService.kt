package com.future.tailormade_design_detail.core.service

import com.future.tailormade.base.model.response.BaseSingleObjectResponse
import com.future.tailormade_design_detail.core.api.DesignDetailApiUrl
import com.future.tailormade_design_detail.core.model.request.DesignRequest
import com.future.tailormade_design_detail.core.model.response.DesignDetailResponse
import com.future.tailormade_design_detail.core.model.response.UploadImageResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface DesignDetailService {

  @GET(DesignDetailApiUrl.DESIGNS_ID_PATH)
  suspend fun getDesignDetailById(
      @Path("id") id: String): BaseSingleObjectResponse<DesignDetailResponse>

  @Multipart
  @POST(DesignDetailApiUrl.UPLOAD_PATH)
  suspend fun postUploadImage(@Path("filePath") filePath: String,
      @Part file: MultipartBody.Part): BaseSingleObjectResponse<UploadImageResponse>

  @POST(DesignDetailApiUrl.TAILORS_ID_DESIGNS_PATH)
  suspend fun postAddDesignByTailor(@Path("tailorId") tailorId: String,
      @Body designDetailRequest: DesignRequest): BaseSingleObjectResponse<DesignDetailResponse>

  @PUT(DesignDetailApiUrl.TAILORS_ID_DESIGNS_ID_PATH)
  suspend fun putEditDesignByTailorAndById(@Path("tailorId") tailorId: String, @Path("id") id: String,
      @Body designDetailRequest: DesignRequest): BaseSingleObjectResponse<DesignDetailResponse>
}