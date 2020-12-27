package com.future.tailormade_design_detail.feature.designDetail.viewModel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.util.extension.onError
import com.future.tailormade.util.extension.toIndonesiaCurrencyFormat
import com.future.tailormade_auth.core.repository.impl.AuthSharedPrefRepository
import com.future.tailormade_design_detail.core.model.DesignDetailUiModel
import com.future.tailormade_design_detail.core.model.SizeDetailUiModel
import com.future.tailormade_design_detail.core.model.SizeUiModel
import com.future.tailormade_design_detail.core.model.response.DesignDetailResponse
import com.future.tailormade_design_detail.core.model.response.SizeDetailResponse
import com.future.tailormade_design_detail.core.model.response.SizeResponse
import com.future.tailormade_design_detail.core.repository.DesignDetailRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart

class DesignDetailViewModel @ViewModelInject constructor(
    private val designDetailRepository: DesignDetailRepository,
    private val authSharedPrefRepository: AuthSharedPrefRepository,
    @Assisted private val savedStateHandle: SavedStateHandle) : BaseViewModel() {

  companion object {
    private const val DESIGN_DETAIL = "DESIGN_DETAIL"
  }

  private var _designDetailUiModel = MutableLiveData<DesignDetailUiModel>()
  val designDetailUiModel: LiveData<DesignDetailUiModel>
    get() = _designDetailUiModel

  init {
    _designDetailUiModel = savedStateHandle.getLiveData(DESIGN_DETAIL, null)
  }

  override fun getLogName() = "com.future.tailormade_design_detail.feature.designDetail.viewModel.DesignDetailViewModel"

  @ExperimentalCoroutinesApi
  fun fetchDesignDetailData(id: String) {
    launchViewModelScope {
      designDetailRepository.getDesignDetailById(id).onStart {
        setStartLoading()
      }.onError {
        setFinishLoading()
        setErrorMessage("Failed to get data. Please try again.")
      }.collectLatest { response ->
        response.data?.let { it ->
          _designDetailUiModel.value = mapToDesignDetailUiModel(it)
          savedStateHandle.set(DESIGN_DETAIL, _designDetailUiModel.value)
        }
        setFinishLoading()
      }
    }
  }

  private fun mapToDesignDetailUiModel(designDetailResponse: DesignDetailResponse) = DesignDetailUiModel(
      id = designDetailResponse.id, title = designDetailResponse.title,
      description = designDetailResponse.description, tailorId = designDetailResponse.tailorId,
      tailorName = designDetailResponse.tailorName, image = designDetailResponse.image,
      price = designDetailResponse.price.toIndonesiaCurrencyFormat(),
      discount = setDiscount(designDetailResponse.price, designDetailResponse.discount),
      category = designDetailResponse.category, color = designDetailResponse.color,
      size = setSize(designDetailResponse.size))

  private fun setDiscount(price: Double, discount: Double) = if (discount > 0.0) {
    (price - discount).toIndonesiaCurrencyFormat()
  } else {
    null
  }

  private fun setSize(sizes: List<SizeResponse>): MutableList<SizeUiModel> {
    val sizesUiModel = mutableListOf<SizeUiModel>()
    sizes.forEach { size ->
      val sizeUiModel = SizeUiModel(id = size.id)
      size.detail?.let {
        sizeUiModel.detail = setSizeDetailUiModel(size.detail)
      }
      sizesUiModel.add(sizeUiModel)
    }
    return sizesUiModel
  }

  private fun setSizeDetailUiModel(sizeDetailResponse: SizeDetailResponse): SizeDetailUiModel {
    val sizeDetailUiModel = SizeDetailUiModel()
    with(sizeDetailResponse) {
      chest?.let { sizeDetailUiModel.chest = it.toString() }
      hips?.let { sizeDetailUiModel.hips = it.toString() }
      waist?.let { sizeDetailUiModel.waist = it.toString() }
      inseam?.let { sizeDetailUiModel.inseam = it.toString() }
      neckToWaist?.let { sizeDetailUiModel.neckToWaist = it.toString() }
    }
    return sizeDetailUiModel
  }
}