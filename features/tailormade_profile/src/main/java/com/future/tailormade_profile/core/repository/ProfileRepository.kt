package com.future.tailormade_profile.core.repository

import com.future.tailormade.base.model.response.BaseSingleObjectResponse
import com.future.tailormade_profile.core.model.request.UpdateProfileAboutRequest
import com.future.tailormade_profile.core.model.request.UpdateProfileRequest
import com.future.tailormade_profile.core.model.response.LocationResponse
import com.future.tailormade_profile.core.model.response.ProfileAboutResponse
import com.future.tailormade_profile.core.model.response.ProfileInfoResponse
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {

  suspend fun getProfileInfo(id: String): Flow<BaseSingleObjectResponse<ProfileInfoResponse>>

  suspend fun searchLocation(query: String): Flow<ArrayList<LocationResponse>>

  suspend fun updateProfileAbout(id: String,
      updateProfileAboutRequest: UpdateProfileAboutRequest):
      Flow<BaseSingleObjectResponse<ProfileAboutResponse>>

  suspend fun updateProfileInfo(id: String, updateProfileRequest: UpdateProfileRequest):
      Flow<BaseSingleObjectResponse<ProfileInfoResponse>>
}