package com.future.tailormade_design_detail.core.repository

import com.future.tailormade.base.model.BaseMapperModel
import com.future.tailormade.base.model.response.BaseSingleObjectResponse
import com.future.tailormade_design_detail.core.model.request.DesignRequest
import com.future.tailormade_design_detail.core.model.response.DesignDetailResponse
import com.future.tailormade_design_detail.core.model.ui.DesignDetailUiModel
import kotlinx.coroutines.flow.Flow

interface DesignDetailRepository {

  suspend fun getDesignDetailById(
      id: String): Flow<BaseMapperModel<DesignDetailResponse, DesignDetailUiModel>>

  suspend fun addDesignByTailor(
      tailorId: String, designRequest: DesignRequest):
      Flow<BaseSingleObjectResponse<DesignDetailResponse>>

  suspend fun updateDesignById(
      tailorId: String, id: String, designRequest: DesignRequest):
      Flow<BaseMapperModel<DesignDetailResponse, DesignDetailUiModel>>
}