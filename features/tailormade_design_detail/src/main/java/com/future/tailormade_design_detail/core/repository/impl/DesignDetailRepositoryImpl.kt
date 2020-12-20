package com.future.tailormade_design_detail.core.repository.impl

import com.future.tailormade.base.repository.BaseRepository
import com.future.tailormade.util.extension.flowOnIO
import com.future.tailormade_design_detail.core.repository.DesignDetailRepository
import com.future.tailormade_design_detail.core.service.DesignDetailService
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DesignDetailRepositoryImpl @Inject constructor(
    private val designDetailService: DesignDetailService) : BaseRepository(),
    DesignDetailRepository {

  override fun getLogName() = "com.future.tailormade_design_detail.core.repository.impl.DesignDetailRepositoryImpl"

  override fun getDesignDetailById(id: String) = flow {
    emit(designDetailService.getDesignDetailById(id))
  }.flowOnIO()
}