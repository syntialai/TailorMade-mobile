package com.future.tailormade.core.repository.impl

import com.future.tailormade.base.repository.BaseRepository
import com.future.tailormade.core.mapper.DashboardMapper
import com.future.tailormade.core.repository.DashboardRepository
import com.future.tailormade.core.service.DashboardService
import com.future.tailormade.util.extension.flowOnIO
import javax.inject.Inject
import kotlinx.coroutines.flow.flow

class DashboardRepositoryImpl @Inject constructor(private val dashboardService: DashboardService) :
    BaseRepository(), DashboardRepository {

  override fun getLogName() = "com.future.tailormade.core.repository.impl.DashboardRepositoryImpl"

  override suspend fun getDashboardTailors(page: Int, itemPerPage: Int) = flow {
    val tailors = dashboardService.getDashboardTailors(page, itemPerPage).data
    emit(tailors?.map {
      DashboardMapper.mapToDashboardTailorUiModel(it)
    } as ArrayList)
//    emit(DataMock.getDashboardTailorsMock())
  }.flowOnIO()
}