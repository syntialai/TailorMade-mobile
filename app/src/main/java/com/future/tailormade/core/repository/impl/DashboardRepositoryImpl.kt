package com.future.tailormade.core.repository.impl

import com.future.tailormade.base.model.response.BaseListResponse
import com.future.tailormade.base.repository.BaseRepository
import com.future.tailormade.core.model.response.dashboard.DashboardTailorResponse
import com.future.tailormade.core.repository.DashboardRepository
import com.future.tailormade.core.service.DashboardService
import com.future.tailormade.util.extension.flowOnIO
import com.future.tailormade_auth.core.repository.impl.AuthSharedPrefRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DashboardRepositoryImpl @Inject constructor(
    private val dashboardService: DashboardService,
    private val authSharedPrefRepository: AuthSharedPrefRepository) :
    BaseRepository(), DashboardRepository {

  override fun getLogName() = "com.future.tailormade.core.repository.impl.DashboardRepositoryImpl"

  override suspend fun getDashboardTailors(lat: Double, lon: Double, page: Int,
      itemPerPage: Int): Flow<BaseListResponse<DashboardTailorResponse>> = flow {
    emit(dashboardService.getDashboardTailors(lat, lon, page, itemPerPage))
  }.flowOnIO()
}