package com.future.tailormade_design_detail.core.repository.impl

import com.future.tailormade.base.model.BaseMapperModel
import com.future.tailormade.base.repository.BaseRepository
import com.future.tailormade.config.Constants
import com.future.tailormade.util.extension.flowOnIO
import com.future.tailormade_design_detail.core.mapper.DesignDetailMapper
import com.future.tailormade_design_detail.core.model.request.cart.AddToCartRequest
import com.future.tailormade_design_detail.core.model.request.design.DesignRequest
import com.future.tailormade_design_detail.core.model.response.DesignDetailResponse
import com.future.tailormade_design_detail.core.repository.DesignDetailRepository
import com.future.tailormade_design_detail.core.service.DesignDetailService
import java.io.File
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody

class DesignDetailRepositoryImpl @Inject constructor(
    private val designDetailService: DesignDetailService) : BaseRepository(),
    DesignDetailRepository {

  companion object {
    private const val FILE = "file"
  }

  override fun getLogName() = "com.future.tailormade_design_detail.core.repository.impl.DesignDetailRepositoryImpl"

  override suspend fun getDesignDetailById(id: String) = flow {
    val designDetailResponse = designDetailService.getDesignDetailById(id).data
//    val designDetailResponse = DataMock.getDesignDetailMock()
    designDetailResponse?.let { data ->
      val designDetailUiModel = DesignDetailMapper.mapToDesignDetailUiModel(data)
      emit(BaseMapperModel(data, designDetailUiModel))
    }
  }.flowOnIO()

  override suspend fun addDesignByTailor(
      tailorId: String, designRequest: DesignRequest): Flow<DesignDetailResponse> = flow {
    designDetailService.postAddDesignByTailor(tailorId, designRequest).data?.let {
      emit(it)
    }
  }.flowOnIO()

  override suspend fun addToCart(userId: String, addToCartRequest: AddToCartRequest) = flow {
    emit(designDetailService.postAddToCart(userId, addToCartRequest))
  }.flowOnIO()

  override suspend fun updateDesignById(
      tailorId: String, id: String, designRequest: DesignRequest) = flow {
    val response = designDetailService.putEditDesignByTailorAndById(tailorId, id, designRequest).data
    response?.let { data ->
      val designDetailUiModel = DesignDetailMapper.mapToDesignDetailUiModel(data)
      emit(BaseMapperModel(data, designDetailUiModel))
    }
  }.flowOnIO()

  override suspend fun uploadDesignImage(file: File) = flow {
    if (file.exists().not()) {
      emit("")
      return@flow
    }

    val imageMultiPartBody = MultipartBody.Part.createFormData(FILE, file.name,
        RequestBody.create(MediaType.parse(Constants.TYPE_IMAGE_JPEG), file))
    emit(designDetailService.postUploadImage(
        Constants.UPLOAD_TYPE_DESIGN, imageMultiPartBody).data?.imageUrl.orEmpty())
  }.flowOnIO()
}