package com.future.tailormade.core.repository

import com.future.tailormade.base.model.response.BaseListResponse
import com.future.tailormade.core.model.response.dashboard.DashboardTailorResponse
import kotlinx.coroutines.flow.Flow

interface DashboardRepository {

  suspend fun getDashboardTailors(lat: Double, lon: Double, page: Int,
      itemPerPage: Int): Flow<BaseListResponse<DashboardTailorResponse>>
}