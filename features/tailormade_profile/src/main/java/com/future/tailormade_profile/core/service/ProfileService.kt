package com.future.tailormade_profile.core.service

import com.future.tailormade.base.model.response.BaseListResponse
import com.future.tailormade.base.model.response.BaseSingleObjectResponse
import com.future.tailormade_profile.core.api.ProfileApiUrl
import com.future.tailormade_profile.core.model.request.UpdateProfileAboutRequest
import com.future.tailormade_profile.core.model.request.UpdateProfileRequest
import com.future.tailormade_profile.core.model.response.ProfileAboutResponse
import com.future.tailormade_profile.core.model.response.ProfileDesignResponse
import com.future.tailormade_profile.core.model.response.ProfileInfoResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ProfileService {

  @GET(ProfileApiUrl.USERS_ID_PATH)
  suspend fun getProfileInfo(
      @Path("id") id: String): BaseSingleObjectResponse<ProfileInfoResponse>

  @GET(ProfileApiUrl.TAILORS_ID_PATH)
  suspend fun getTailorProfileInfo(
      @Path("tailorId") tailorId: String): BaseSingleObjectResponse<ProfileInfoResponse>

  @GET(ProfileApiUrl.TAILORS_ID_DESIGNS_PATH)
  suspend fun getProfileTailorDesigns(
      @Path("tailorId") id: String, @Query("page") page: Int,
      @Query("itemPerPage") itemPerPage: Int): BaseListResponse<ProfileDesignResponse>

  @PUT(ProfileApiUrl.USERS_ID_UPDATE_BASIC_INFO_PATH)
  suspend fun updateProfileInfo(@Path("id") id: String,
      @Body updateProfileRequest: UpdateProfileRequest):
      BaseSingleObjectResponse<ProfileInfoResponse>

  @PUT(ProfileApiUrl.USERS_ID_UPDATE_MORE_INFO_PATH)
  suspend fun updateProfileAboutInfo(@Path("id") id: String,
      @Body updateProfileAboutRequest: UpdateProfileAboutRequest):
      BaseSingleObjectResponse<ProfileAboutResponse>
}