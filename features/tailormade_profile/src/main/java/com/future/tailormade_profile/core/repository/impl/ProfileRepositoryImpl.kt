package com.future.tailormade_profile.core.repository.impl

import com.future.tailormade.base.model.BaseMapperModel
import com.future.tailormade.util.extension.flowOnIO
import com.future.tailormade_profile.core.mapper.ProfileMapper
import com.future.tailormade_profile.core.model.request.UpdateProfileAboutRequest
import com.future.tailormade_profile.core.model.request.UpdateProfileRequest
import com.future.tailormade_profile.core.model.response.ProfileInfoResponse
import com.future.tailormade_profile.core.model.ui.ProfileInfoUiModel
import com.future.tailormade_profile.core.repository.ProfileRepository
import com.future.tailormade_profile.core.service.NominatimService
import com.future.tailormade_profile.core.service.ProfileService
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ProfileRepositoryImpl @Inject constructor(
    private var profileService: ProfileService,
    private var nominatimService: NominatimService) : ProfileRepository {

  override suspend fun getProfileInfo(id: String) = flow {
    val data = profileService.getProfileInfo(id).data
//    val data = DataMock.getProfileInfoMock()
    data?.let { response ->
      val uiModel = ProfileMapper.mapToProfileInfoUiModel(response)
      emit(BaseMapperModel(response, uiModel))
    }
  }.flowOnIO()

  override suspend fun getTailorProfileInfo(tailorId: String): Flow<ProfileInfoUiModel> = flow {
    val data = profileService.getTailorProfileInfo(tailorId).data
//    val data = DataMock.getProfileInfoMock()
    data?.let {
      emit(ProfileMapper.mapToProfileInfoUiModel(it))
    }
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
      updateProfileRequest: UpdateProfileRequest): Flow<ProfileInfoResponse> = flow {
    profileService.updateProfileInfo(id, updateProfileRequest).data?.let {
      emit(it)
    }
//    emit(DataMock.getProfileInfoMock())
  }
}