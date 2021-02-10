package com.future.tailormade_profile.core.repository.impl

import com.future.tailormade.base.model.BaseMapperModel
import com.future.tailormade_profile.core.mapper.ProfileMapper
import com.future.tailormade_profile.core.model.request.UpdateProfileAboutRequest
import com.future.tailormade_profile.core.model.request.UpdateProfileRequest
import com.future.tailormade_profile.core.model.response.ProfileInfoResponse
import com.future.tailormade_profile.core.model.ui.ProfileInfoUiModel
import com.future.tailormade_profile.core.repository.ProfileRepository
import com.future.tailormade_profile.core.service.NominatimService
import com.future.tailormade_profile.core.service.ProfileService
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class ProfileRepositoryImpl @Inject constructor(private val profileService: ProfileService,
    private val nominatimService: NominatimService, private val ioDispatcher: CoroutineDispatcher) :
    ProfileRepository {

  override suspend fun getProfileInfo(id: String) = flow {
    profileService.getProfileInfo(id).data?.let { response ->
      val uiModel = ProfileMapper.mapToProfileInfoUiModel(response)
      emit(BaseMapperModel(response, uiModel))
    }
  }.flowOn(ioDispatcher)

  override suspend fun getTailorProfileInfo(tailorId: String): Flow<ProfileInfoUiModel> = flow {
    profileService.getTailorProfileInfo(tailorId).data?.let {
      emit(ProfileMapper.mapToProfileInfoUiModel(it))
    }
  }.flowOn(ioDispatcher)

  override suspend fun getProfileDesigns(id: String, page: Int, itemPerPage: Int) = flow {
    profileService.getProfileTailorDesigns(id, page, itemPerPage).data?.let {
      emit(it as ArrayList)
    }
  }.flowOn(ioDispatcher)

  override suspend fun searchLocation(query: String) = flow {
    emit(nominatimService.searchLocation(query, "json", "1", "5", "1"))
  }.flowOn(ioDispatcher)

  override suspend fun updateProfileAbout(id: String,
      updateProfileAboutRequest: UpdateProfileAboutRequest) = flow {
    profileService.updateProfileAboutInfo(id, updateProfileAboutRequest).data?.let {
      emit(it)
    }
  }.flowOn(ioDispatcher)

  override suspend fun updateProfileInfo(id: String, updateProfileRequest: UpdateProfileRequest): Flow<ProfileInfoResponse> = flow {
    profileService.updateProfileInfo(id, updateProfileRequest).data?.let {
      emit(it)
    }
  }.flowOn(ioDispatcher)
}