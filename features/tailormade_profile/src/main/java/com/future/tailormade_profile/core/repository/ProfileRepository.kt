package com.future.tailormade_profile.core.repository

import com.future.tailormade.base.model.response.BaseSingleObjectResponse
import com.future.tailormade_profile.core.model.request.UpdateProfileRequest
import com.future.tailormade_profile.core.model.response.LocationResponse
import com.future.tailormade_profile.core.model.response.ProfileInfoResponse
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {

  suspend fun getProfileInfo(id: String): Flow<BaseSingleObjectResponse<ProfileInfoResponse>>

  suspend fun searchLocation(query: String): Flow<List<LocationResponse>>

  suspend fun updateProfileInfo(id: String, request: UpdateProfileRequest):
      Flow<BaseSingleObjectResponse<ProfileInfoResponse>>
}