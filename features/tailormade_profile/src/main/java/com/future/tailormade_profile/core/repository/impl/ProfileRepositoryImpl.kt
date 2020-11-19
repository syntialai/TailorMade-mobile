package com.future.tailormade_profile.core.repository.impl

import com.future.tailormade.base.model.response.BaseSingleObjectResponse
import com.future.tailormade_profile.core.di.scope.ProfileScope
import com.future.tailormade_profile.core.model.request.UpdateProfileRequest
import com.future.tailormade_profile.core.model.response.LocationResponse
import com.future.tailormade_profile.core.model.response.ProfileInfoResponse
import com.future.tailormade_profile.core.repository.ProfileRepository
import com.future.tailormade_profile.core.service.NominatimService
import com.future.tailormade_profile.core.service.ProfileService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ProfileScope
class ProfileRepositoryImpl @Inject constructor(
    private var profileService: ProfileService,
    private var nominatimService: NominatimService) : ProfileRepository {

  override suspend fun getProfileInfo(
      id: String): Flow<BaseSingleObjectResponse<ProfileInfoResponse>> = flow {
    emit(profileService.getProfileInfo(id))
  }

  override suspend fun searchLocation(
      query: String): Flow<List<LocationResponse>> = flow {
    emit(nominatimService.searchLocation(query, "json", 1, 5))
  }

  override suspend fun updateProfileInfo(id: String,
      request: UpdateProfileRequest): Flow<BaseSingleObjectResponse<ProfileInfoResponse>> = flow {
    emit(profileService.updateProfileInfo(id, request))
  }
}