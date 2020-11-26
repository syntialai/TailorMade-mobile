package com.future.tailormade_profile.core.service

import com.future.tailormade.base.model.response.BaseSingleObjectResponse
import com.future.tailormade_profile.core.api.ProfileApiUrl
import com.future.tailormade_profile.core.model.request.UpdateProfileAboutRequest
import com.future.tailormade_profile.core.model.request.UpdateProfileRequest
import com.future.tailormade_profile.core.model.response.ProfileAboutResponse
import com.future.tailormade_profile.core.model.response.ProfileInfoResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProfileService {

  @GET(ProfileApiUrl.USERS_ID_PATH)
  suspend fun getProfileInfo(
      @Path("id") id: String): BaseSingleObjectResponse<ProfileInfoResponse>

  @PUT(ProfileApiUrl.USERS_ID_UPDATE_BASIC_INFO_PATH)
  suspend fun updateProfileInfo(@Path("id") id: String,
      @Body updateProfileRequest: UpdateProfileRequest): BaseSingleObjectResponse<ProfileInfoResponse>

  @PUT(ProfileApiUrl.USERS_ID_UPDATE_MORE_INFO_PATH)
  suspend fun updateProfileAboutInfo(@Path("id") id: String,
      @Body updateProfileAboutRequest: UpdateProfileAboutRequest): BaseSingleObjectResponse<ProfileAboutResponse>
}