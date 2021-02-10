package com.future.tailormade.tailor_app.core.repository

import com.future.tailormade.base.model.response.BaseResponse
import com.future.tailormade.tailor_app.core.model.ui.dashboard.DashboardDesignUiModel
import kotlinx.coroutines.flow.Flow

interface DashboardRepository {

  suspend fun getDashboardDesigns(
      id: String, page: Int, itemPerPage: Int): Flow<ArrayList<DashboardDesignUiModel>>

  suspend fun deleteDashboardDesign(tailorId: String, id: String): Flow<BaseResponse>
}