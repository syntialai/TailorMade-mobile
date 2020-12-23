package com.future.tailormade.tailor_app.core.repository.impl

import com.future.tailormade.base.repository.BaseRepository
import com.future.tailormade.tailor_app.core.mapper.DashboardMapper
import com.future.tailormade.tailor_app.core.repository.DashboardRepository
import com.future.tailormade.util.extension.flowOnIO
import com.future.tailormade_profile.core.service.ProfileService
import javax.inject.Inject
import kotlinx.coroutines.flow.flow

class DashboardRepositoryImpl @Inject constructor(private val profileService: ProfileService) :
    BaseRepository(), DashboardRepository {

  override fun getLogName() =
      "com.future.tailormade.tailor_app.core.repository.impl.DashboardRepositoryImpl"

  override suspend fun getDashboardDesigns(id: String, page: Int, itemPerPage: Int) = flow {
    val designs = profileService.getProfileTailorDesigns(id, page, itemPerPage).data
    emit(designs?.map {
      DashboardMapper.mapToDashboardDesignUiModel(it)
    } as ArrayList)
  }.flowOnIO()
}