package com.future.tailormade.core.repository.impl

import com.future.tailormade.base.repository.BaseRepository
import com.future.tailormade.core.mapper.DashboardMapper
import com.future.tailormade.core.repository.DashboardRepository
import com.future.tailormade.core.service.DashboardService
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class DashboardRepositoryImpl @Inject constructor(private val dashboardService: DashboardService,
    private val ioDispatcher: CoroutineDispatcher) : BaseRepository(), DashboardRepository {

  override fun getLogName() = "com.future.tailormade.core.repository.impl.DashboardRepositoryImpl"

  override suspend fun getDashboardTailors(page: Int, itemPerPage: Int) = flow {
    dashboardService.getDashboardTailors(page, itemPerPage).data?.let { list ->
      emit(list.map {
        DashboardMapper.mapToDashboardTailorUiModel(it)
      } as ArrayList)
    }
  }.flowOn(ioDispatcher)
}