package com.future.tailormade_design_detail.feature.designDetail.viewModel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.future.tailormade.base.repository.AuthSharedPrefRepository
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.config.Constants
import com.future.tailormade.util.extension.onError
import com.future.tailormade_design_detail.core.model.request.cart.AddToCartDesignRequest
import com.future.tailormade_design_detail.core.model.request.cart.AddToCartRequest
import com.future.tailormade_design_detail.core.model.response.DesignDetailResponse
import com.future.tailormade_design_detail.core.model.ui.DesignDetailUiModel
import com.future.tailormade_design_detail.core.repository.DesignDetailRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart

class DesignDetailViewModel @ViewModelInject constructor(
    private val designDetailRepository: DesignDetailRepository,
    private val authSharedPrefRepository: AuthSharedPrefRepository,
    @Assisted private val savedStateHandle: SavedStateHandle) : BaseViewModel() {

  companion object {
    private const val DESIGN_DETAIL_RESPONSE = "DESIGN_DETAIL_RESPONSE"
    private const val DESIGN_DETAIL_UI_MODEL = "DESIGN_DETAIL_UI_MODEL"
  }

  private var _designDetailResponse: MutableLiveData<DesignDetailResponse>
  val designDetailResponse: LiveData<DesignDetailResponse>
    get() = _designDetailResponse

  private var _designDetailUiModel: MutableLiveData<DesignDetailUiModel>
  val designDetailUiModel: LiveData<DesignDetailUiModel>
    get() = _designDetailUiModel

  private var _isAddedToCart = MutableLiveData<Pair<String, String?>>()
  val isAddedToCart: LiveData<Pair<String, String?>>
    get() = _isAddedToCart

  private var addToCartRequest: AddToCartRequest? = null

  init {
    _designDetailResponse = savedStateHandle.getLiveData(DESIGN_DETAIL_RESPONSE)
    _designDetailUiModel = savedStateHandle.getLiveData(DESIGN_DETAIL_UI_MODEL)
  }

  override fun getLogName() =
      "com.future.tailormade_design_detail.feature.designDetail.viewModel.DesignDetailViewModel"

  fun addToCart(type: String) {
    authSharedPrefRepository.userId?.let { id ->
      addToCartRequest?.let { request ->
        launchViewModelScope {
          designDetailRepository.addToCart(id, request).onStart {
            setStartLoading()
          }.onError {
            setFinishLoading()
            setErrorMessage(Constants.generateFailedAddError("cart"))
            _isAddedToCart.value = Pair(type, null)
          }.collectLatest {
            setFinishLoading()
            _isAddedToCart.value = Pair(type, it.wishlistId)
          }
        }
      }
    }
  }

  @ExperimentalCoroutinesApi
  @InternalCoroutinesApi
  fun fetchDesignDetailData(id: String) {
    launchViewModelScope {
      designDetailRepository.getDesignDetailById(id).onError {
        setErrorMessage(Constants.generateFailedFetchError("design"))
      }.collectLatest { data ->
        setResponse(data.response)
        setUiModel(data.uiModel)
      }
    }
  }

  fun setAddToCartRequest(designDetail: DesignDetailResponse) {
    val designRequest = AddToCartDesignRequest(id = designDetail.id, title = designDetail.title,
        image = designDetail.image, price = designDetail.price, discount = designDetail.discount,
        size = designDetail.size[0].name, sizeDetail = designDetail.size[0].detail,
        color = designDetail.color[0].name)
    addToCartRequest = AddToCartRequest(userName = authSharedPrefRepository.name,
        tailorId = designDetail.tailorId, tailorName = designDetail.tailorName, quantity = 1,
        design = designRequest)
  }

  fun setColorRequest(color: String) {
    addToCartRequest?.design?.color = color
  }

  fun setSizeRequest(index: Int) {
    val size = _designDetailResponse.value?.size?.get(index)
    addToCartRequest?.design?.let {
      it.size = size?.name.orEmpty()
      it.sizeDetail = size?.detail
    }
  }

  private fun setResponse(designDetailResponse: DesignDetailResponse) {
    _designDetailResponse.value = designDetailResponse
    savedStateHandle.set(DESIGN_DETAIL_RESPONSE, _designDetailResponse.value)
  }

  private fun setUiModel(designDetailUiModel: DesignDetailUiModel) {
    _designDetailUiModel.value = designDetailUiModel
    savedStateHandle.set(DESIGN_DETAIL_UI_MODEL, _designDetailUiModel.value)
  }
}