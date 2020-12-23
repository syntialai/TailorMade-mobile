package com.future.tailormade.tailor_app.core.repository

import com.future.tailormade.tailor_app.core.model.ui.DashboardDesignUiModel
import kotlinx.coroutines.flow.Flow

interface DashboardRepository {

  suspend fun getDashboardDesigns(
      id: String, page: Int, itemPerPage: Int): Flow<ArrayList<DashboardDesignUiModel>>
}