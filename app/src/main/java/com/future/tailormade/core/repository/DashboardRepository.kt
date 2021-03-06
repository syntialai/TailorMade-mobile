package com.future.tailormade.core.repository

import com.future.tailormade.core.model.ui.dashboard.DashboardTailorUiModel
import kotlinx.coroutines.flow.Flow

interface DashboardRepository {

  suspend fun getDashboardTailors(
      page: Int, itemPerPage: Int): Flow<ArrayList<DashboardTailorUiModel>>
}