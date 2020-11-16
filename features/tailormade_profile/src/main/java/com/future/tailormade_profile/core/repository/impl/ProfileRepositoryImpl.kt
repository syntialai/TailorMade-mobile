package com.future.tailormade_profile.core.repository.impl

import com.future.tailormade_profile.core.model.request.UpdateProfileRequest
import com.future.tailormade_profile.core.repository.ProfileRepository
import com.future.tailormade_profile.core.service.NominatimService
import com.future.tailormade_profile.core.service.ProfileService
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private var profileService: ProfileService,
    private var nominatimService: NominatimService) : ProfileRepository {

  override suspend fun getProfileInfo(id: String) = flow {
    emit(profileService.getProfileInfo(id))
  }

  override suspend fun searchLocation(query: String) = flow {
    emit(nominatimService.searchLocation(query, "json", 1, 5))
  }

  override suspend fun updateProfileInfo(id: String,
      request: UpdateProfileRequest) = flow {
    emit(profileService.updateProfileInfo(id, request))
  }
}