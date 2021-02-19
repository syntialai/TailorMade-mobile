package com.future.tailormade.tailor_app.core.repository.impl

import com.future.tailormade.base.repository.BaseRepository
import com.future.tailormade.tailor_app.core.mapper.DashboardMapper
import com.future.tailormade.tailor_app.core.repository.DashboardRepository
import com.future.tailormade.tailor_app.core.service.DashboardService
import com.future.tailormade_profile.core.service.ProfileService
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class DashboardRepositoryImpl @Inject constructor(private val profileService: ProfileService,
    private val dashboardService: DashboardService, private val ioDispatcher: CoroutineDispatcher) :
    BaseRepository(), DashboardRepository {

  override fun getLogName() =
      "com.future.tailormade.tailor_app.core.repository.impl.DashboardRepositoryImpl"

  override suspend fun getDashboardDesigns(id: String, page: Int, itemPerPage: Int) = flow {
    profileService.getProfileTailorDesigns(id, page, itemPerPage).data?.let {
      emit(it.map { design ->
        DashboardMapper.mapToDashboardDesignUiModel(design)
      } as ArrayList)
    }
  }.flowOn(ioDispatcher)

  override suspend fun deleteDashboardDesign(tailorId: String, id: String) = flow {
    emit(dashboardService.deleteDesignById(tailorId, id))
  }.flowOn(ioDispatcher)
}