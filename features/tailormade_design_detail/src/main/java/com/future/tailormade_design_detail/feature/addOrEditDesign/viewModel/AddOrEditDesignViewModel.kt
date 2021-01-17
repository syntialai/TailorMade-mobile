package com.future.tailormade_design_detail.feature.addOrEditDesign.viewModel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.config.Constants
import com.future.tailormade.util.extension.onError
import com.future.tailormade.util.image.ImageHelper
import com.future.tailormade_auth.core.repository.impl.AuthSharedPrefRepository
import com.future.tailormade_design_detail.core.model.request.DesignColorRequest
import com.future.tailormade_design_detail.core.model.request.DesignRequest
import com.future.tailormade_design_detail.core.model.request.DesignSizeDetailRequest
import com.future.tailormade_design_detail.core.model.request.DesignSizeRequest
import com.future.tailormade_design_detail.core.model.response.DesignDetailResponse
import com.future.tailormade_design_detail.core.model.ui.SizeDetailUiModel
import com.future.tailormade_design_detail.core.repository.DesignDetailRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart

class AddOrEditDesignViewModel @ViewModelInject constructor(
    private val designDetailRepository: DesignDetailRepository,
    private val authSharedPrefRepository: AuthSharedPrefRepository,
    @Assisted private val savedStateHandle: SavedStateHandle) : BaseViewModel() {

  override fun getLogName() =
      "com.future.tailormade_design_detail.feature.addOrEditDesign.viewModel.AddOrEditDesignViewModel"

  companion object {
    private const val DESIGN_DETAIL_RESPONSE = "DESIGN_DETAIL_RESPONSE"
  }

  private var _designDetailResponse: MutableLiveData<DesignDetailResponse>
  val designDetailResponse: LiveData<DesignDetailResponse>
    get() = _designDetailResponse

  private var colorRequest: MutableList<DesignColorRequest> = mutableListOf()
  private var imageRequest: String = ""
  private var sizeRequest: MutableList<DesignSizeRequest> = mutableListOf()

  init {
    _designDetailResponse = savedStateHandle.getLiveData(DESIGN_DETAIL_RESPONSE)
  }

  fun isPriceValid(price: String, discount: String) = price.toDouble() > discount.toDouble()

  fun addColor(name: String, color: String) {
    colorRequest.add(getDesignColorRequest(name, color))
  }

  fun addSize(name: String, sizeDetail: SizeDetailUiModel) {
    sizeRequest.add(getDesignSizeRequest(name, sizeDetail))
  }

  fun addDesign(title: String, price: String, discount: String, description: String) {
    launchViewModelScope {
      authSharedPrefRepository.userId?.let { tailorId ->
        designDetailRepository.addDesignByTailor(tailorId,
            getDesignRequest(title, price, discount, description)).onStart {
          setStartLoading()
        }.onError {
          setErrorMessage(Constants.FAILED_TO_UPDATE_DESIGN)
          setFinishLoading()
        }.collectLatest {
          // TODO: update
          setFinishLoading()
        }
      }
    }
  }

  fun updateDesign(title: String, price: String, discount: String, description: String) {
    launchViewModelScope {
      authSharedPrefRepository.userId?.let { tailorId ->
        designDetailRepository.updateDesignById(tailorId, _designDetailResponse.value?.id.orEmpty(),
            getDesignRequest(title, price, discount, description)).onStart {
          setStartLoading()
        }.onError {
          setErrorMessage(Constants.FAILED_TO_UPDATE_DESIGN)
          setFinishLoading()
        }.collectLatest {
          // TODO: update
          setFinishLoading()
        }
      }
    }
  }

  fun removeColor(name: String, color: String) {
    colorRequest.remove(getDesignColorRequest(name, color))
  }

  fun removeSize(name: String, sizeDetail: SizeDetailUiModel) {
    sizeRequest.remove(getDesignSizeRequest(name, sizeDetail))
  }

  fun setDesignDetailResponse(response: DesignDetailResponse) {
    _designDetailResponse.value = response
  }

  @RequiresApi(Build.VERSION_CODES.O)
  fun setImage(imagePath: String) {
    imageRequest = ImageHelper.encodeFile(imagePath)
  }

  fun validate() = when {
    imageRequest.isBlank() -> {
      setErrorMessage(Constants.IMAGE_MUST_BE_ATTACHED)
      false
    }
    sizeRequest.isEmpty() -> {
      setErrorMessage(Constants.SIZE_IS_EMPTY)
      false
    }
    colorRequest.isEmpty() -> {
      setErrorMessage(Constants.COLOR_IS_EMPTY)
      false
    }
    else -> true
  }

  private fun getDesignColorRequest(name: String, color: String) = DesignColorRequest(name, color)

  private fun getDesignSizeRequest(name: String, sizeDetail: SizeDetailUiModel) = DesignSizeRequest(
      name, mapToSizeDetailRequest(sizeDetail))

  private fun getDesignRequest(
      title: String, price: String, discount: String, description: String) = DesignRequest(
      title = title, price = price.toDouble(), discount = discount.toDouble(),
      description = description, image = imageRequest, color = colorRequest, size = sizeRequest)

  private fun mapToSizeDetailRequest(sizeDetail: SizeDetailUiModel) = DesignSizeDetailRequest(
      sizeDetail.chest.toFloat(),
      sizeDetail.waist.toFloat(),
      sizeDetail.hips.toFloat(),
      sizeDetail.neckToWaist.toFloat(),
      sizeDetail.inseam.toFloat()
  )
}