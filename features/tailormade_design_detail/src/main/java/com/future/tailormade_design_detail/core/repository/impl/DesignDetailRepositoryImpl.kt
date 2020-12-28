package com.future.tailormade_design_detail.core.repository.impl

import com.future.tailormade.base.model.BaseMapperModel
import com.future.tailormade.base.model.response.BaseSingleObjectResponse
import com.future.tailormade.base.repository.BaseRepository
import com.future.tailormade.util.extension.flowOnIO
import com.future.tailormade_design_detail.core.mapper.DesignDetailMapper
import com.future.tailormade_design_detail.core.model.request.DesignRequest
import com.future.tailormade_design_detail.core.model.response.DesignDetailResponse
import com.future.tailormade_design_detail.core.repository.DesignDetailRepository
import com.future.tailormade_design_detail.core.service.DesignDetailService
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DesignDetailRepositoryImpl @Inject constructor(
    private val designDetailService: DesignDetailService) : BaseRepository(),
    DesignDetailRepository {

  override fun getLogName() = "com.future.tailormade_design_detail.core.repository.impl.DesignDetailRepositoryImpl"

  override suspend fun getDesignDetailById(id: String) = flow {
    val designDetailResponse = designDetailService.getDesignDetailById(id).data
    designDetailResponse?.let { data ->
      val designDetailUiModel = DesignDetailMapper.mapToDesignDetailUiModel(data)
      emit(BaseMapperModel(data, designDetailUiModel))
    }
  }.flowOnIO()

  override suspend fun addDesignByTailor(
      tailorId: String, designRequest: DesignRequest): Flow<BaseSingleObjectResponse<DesignDetailResponse>> = flow {
    emit(designDetailService.postAddDesignByTailor(tailorId, designRequest))
  }.flowOnIO()

  override suspend fun updateDesignById(
      tailorId: String, id: String, designRequest: DesignRequest) = flow {
    val response = designDetailService.putEditDesignByTailorAndById(tailorId, id, designRequest).data
    response?.let { data ->
      val designDetailUiModel = DesignDetailMapper.mapToDesignDetailUiModel(data)
      emit(BaseMapperModel(data, designDetailUiModel))
    }
  }.flowOnIO()
}