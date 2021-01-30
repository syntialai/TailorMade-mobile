package com.future.tailormade_design_detail.feature.designDetail.viewModel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.config.Constants
import com.future.tailormade.util.extension.onError
import com.future.tailormade_design_detail.core.model.response.DesignDetailResponse
import com.future.tailormade_design_detail.core.model.ui.DesignDetailUiModel
import com.future.tailormade_design_detail.core.repository.DesignDetailRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest

class DesignDetailViewModel @ViewModelInject constructor(
    private val designDetailRepository: DesignDetailRepository,
    @Assisted private val savedStateHandle: SavedStateHandle) : BaseViewModel() {

  companion object {
    private const val DESIGN_DETAIL_RESPONSE = "DESIGN_DETAIL_RESPONSE"
    private const val DESIGN_DETAIL_UI_MODEL = "DESIGN_DETAIL_UI_MODEL"
  }

  private var _designDetailResponse = MutableLiveData<DesignDetailResponse>()
  val designDetailResponse: LiveData<DesignDetailResponse>
    get() = _designDetailResponse

  private var _designDetailUiModel = MutableLiveData<DesignDetailUiModel>()
  val designDetailUiModel: LiveData<DesignDetailUiModel>
    get() = _designDetailUiModel

  init {
    _designDetailResponse = savedStateHandle.getLiveData(DESIGN_DETAIL_RESPONSE, null)
    _designDetailUiModel = savedStateHandle.getLiveData(DESIGN_DETAIL_UI_MODEL, null)
  }

  override fun getLogName() = "com.future.tailormade_design_detail.feature.designDetail.viewModel.DesignDetailViewModel"

  fun addToCart() {
    launchViewModelScope {

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

  private fun setResponse(designDetailResponse: DesignDetailResponse) {
    _designDetailResponse.value = designDetailResponse
    savedStateHandle.set(DESIGN_DETAIL_RESPONSE, _designDetailResponse.value)
  }

  private fun setUiModel(designDetailUiModel: DesignDetailUiModel) {
    _designDetailUiModel.value = designDetailUiModel
    savedStateHandle.set(DESIGN_DETAIL_UI_MODEL, _designDetailUiModel.value)
  }
}