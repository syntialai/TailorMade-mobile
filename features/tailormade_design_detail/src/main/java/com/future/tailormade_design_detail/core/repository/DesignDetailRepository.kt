package com.future.tailormade_design_detail.core.repository

import com.future.tailormade.base.model.response.BaseSingleObjectResponse
import com.future.tailormade_design_detail.core.model.response.DesignDetailResponse
import kotlinx.coroutines.flow.Flow

interface DesignDetailRepository {

  fun getDesignDetailById(id: String): Flow<BaseSingleObjectResponse<DesignDetailResponse>>
}