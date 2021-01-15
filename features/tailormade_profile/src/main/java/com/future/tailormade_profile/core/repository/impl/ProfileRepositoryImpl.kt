package com.future.tailormade_profile.core.repository.impl

import com.future.tailormade.util.extension.flowOnIO
import com.future.tailormade_profile.core.model.request.UpdateProfileAboutRequest
import com.future.tailormade_profile.core.model.request.UpdateProfileRequest
import com.future.tailormade_profile.core.repository.ProfileRepository
import com.future.tailormade_profile.core.service.NominatimService
import com.future.tailormade_profile.core.service.ProfileService
import javax.inject.Inject
import kotlinx.coroutines.flow.flow

class ProfileRepositoryImpl @Inject constructor(
    private var profileService: ProfileService,
    private var nominatimService: NominatimService) : ProfileRepository {

  override suspend fun getProfileInfo(id: String) = flow {
    emit(profileService.getProfileInfo(id))
  }.flowOnIO()

  override suspend fun getProfileDesigns(id: String, page: Int, itemPerPage: Int) = flow {
    emit(profileService.getProfileTailorDesigns(id, page, itemPerPage))
  }.flowOnIO()

  override suspend fun searchLocation(query: String) = flow {
    emit(nominatimService.searchLocation(query, "json", "1", "5", "1"))
  }.flowOnIO()

  override suspend fun updateProfileAbout(id: String,
      updateProfileAboutRequest: UpdateProfileAboutRequest) = flow {
    emit(profileService.updateProfileAboutInfo(id, updateProfileAboutRequest))
  }

  override suspend fun updateProfileInfo(id: String,
      updateProfileRequest: UpdateProfileRequest) = flow {
    emit(profileService.updateProfileInfo(id, updateProfileRequest))
  }
}