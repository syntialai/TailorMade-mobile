package com.future.tailormade_design_detail.feature.addOrEditDesign.viewModel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.future.tailormade.base.repository.AuthSharedPrefRepository
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.config.Constants
import com.future.tailormade.util.extension.onError
import com.future.tailormade_design_detail.core.model.request.design.DesignColorRequest
import com.future.tailormade_design_detail.core.model.request.design.DesignRequest
import com.future.tailormade_design_detail.core.model.request.design.DesignSizeDetailRequest
import com.future.tailormade_design_detail.core.model.request.design.DesignSizeRequest
import com.future.tailormade_design_detail.core.model.response.DesignDetailResponse
import com.future.tailormade_design_detail.core.model.ui.SizeDetailUiModel
import com.future.tailormade_design_detail.core.repository.DesignDetailRepository
import java.io.File
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.onStart

class AddOrEditDesignViewModel @ViewModelInject constructor(
    private val designDetailRepository: DesignDetailRepository,
    private val authSharedPrefRepository: AuthSharedPrefRepository,
    @Assisted private val savedStateHandle: SavedStateHandle) : BaseViewModel() {

  override fun getLogName() =
      "com.future.tailormade_design_detail.feature.addOrEditDesign.viewModel.AddOrEditDesignViewModel"

  companion object {
    private const val DESIGN_DETAIL_RESPONSE = "DESIGN_DETAIL_RESPONSE"
    private const val IS_UPDATED = "IS_UPDATED"
  }

  private var _designDetailResponse = MutableLiveData<DesignDetailResponse>()
  val designDetailResponse: LiveData<DesignDetailResponse>
    get() = _designDetailResponse

  private var _isUpdated = MutableLiveData<Boolean>()
  val isUpdated: LiveData<Boolean>
    get() = _isUpdated

  private var colorRequest: MutableList<DesignColorRequest> = mutableListOf()
  private var imageRequest: File = File("")
  private var sizeRequest: MutableList<DesignSizeRequest> = mutableListOf()

//  init {
//    _designDetailResponse = savedStateHandle.getLiveData(DESIGN_DETAIL_RESPONSE)
//    _isUpdated = savedStateHandle.getLiveData(IS_UPDATED, false)
//  }

  @FlowPreview
  @ExperimentalCoroutinesApi
  fun addDesign(title: String, price: String, discount: String, description: String) {
    launchViewModelScope {
      authSharedPrefRepository.userId?.let { tailorId ->
        designDetailRepository.uploadDesignImage(imageRequest).onStart {
          setStartLoading()
        }.onError {
          setFinishLoading()
          setErrorMessage(Constants.generateFailedUpdateError("design"))
        }.flatMapConcat { imagePath ->
          designDetailRepository.addDesignByTailor(tailorId,
              getDesignRequest(title, imagePath, price, discount, description))
        }.collectLatest {
          setFinishLoading()
          setDesignDetailResponse(it)
          _isUpdated.value = true
        }
      }
    }
  }

  fun updateDesign(title: String, price: String, discount: String, description: String) {
    launchViewModelScope {
      authSharedPrefRepository.userId?.let { tailorId ->
        designDetailRepository.updateDesignById(tailorId, _designDetailResponse.value?.id.orEmpty(),
            getDesignRequest(title, _designDetailResponse.value?.image.orEmpty(), price, discount,
                description)).onStart {
          setStartLoading()
        }.onError {
          setErrorMessage(Constants.generateFailedUpdateError("design"))
          setFinishLoading()
        }.collectLatest {
          setFinishLoading()
          setDesignDetailResponse(it.response)
          _isUpdated.value = true
        }
      }
    }
  }

  fun isPriceValid(price: String, discount: String) = price.toDouble() > discount.toDouble()

  fun addColor(name: String, color: String) {
    colorRequest.add(getDesignColorRequest(name, color))
  }

  fun addSize(name: String, sizeDetail: SizeDetailUiModel) {
    sizeRequest.add(getDesignSizeRequest(name, sizeDetail))
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

  fun setImageFile(filePath: String) {
    imageRequest = File(filePath)
  }

  fun validate() = when {
    sizeRequest.isEmpty() -> {
      setErrorMessage(Constants.SIZE_IS_EMPTY)
      false
    }
    colorRequest.isEmpty() -> {
      setErrorMessage(Constants.COLOR_IS_EMPTY)
      false
    }
    imageRequest.exists().not() -> {
      setErrorMessage(Constants.IMAGE_MUST_BE_ATTACHED)
      false
    }
    else -> true
  }

  private fun getDesignColorRequest(name: String, color: String) = DesignColorRequest(color = color,
      name = name)

  private fun getDesignSizeRequest(name: String, sizeDetail: SizeDetailUiModel) = DesignSizeRequest(
      name, mapToSizeDetailRequest(sizeDetail))

  private fun getDesignRequest(
      title: String, image: String, price: String, discount: String, description: String) = DesignRequest(
      title = title, price = price.toDouble(), discount = discount.toDouble(),
      description = description, image = image, color = colorRequest, size = sizeRequest)

  private fun mapToSizeDetailRequest(sizeDetail: SizeDetailUiModel) = DesignSizeDetailRequest(
      sizeDetail.chest.substringBeforeLast("cm").toFloat(),
      sizeDetail.waist.substringBeforeLast("cm").toFloat(),
      sizeDetail.hips.substringBeforeLast("cm").toFloat(),
      sizeDetail.neckToWaist.substringBeforeLast("cm").toFloat(),
      sizeDetail.inseam.substringBeforeLast("cm").toFloat()
  )
}