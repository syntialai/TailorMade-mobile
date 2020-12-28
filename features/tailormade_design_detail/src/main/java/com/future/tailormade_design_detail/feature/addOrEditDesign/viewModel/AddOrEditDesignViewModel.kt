package com.future.tailormade_design_detail.feature.addOrEditDesign.viewModel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade_auth.core.repository.impl.AuthSharedPrefRepository
import com.future.tailormade_design_detail.core.model.response.DesignDetailResponse
import com.future.tailormade_design_detail.core.repository.DesignDetailRepository

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

  init {
    _designDetailResponse = savedStateHandle.getLiveData(DESIGN_DETAIL_RESPONSE, null)
  }

  fun setDesignDetailResponse(response: DesignDetailResponse) {
    _designDetailResponse.value = response
    savedStateHandle.set(DESIGN_DETAIL_RESPONSE, _designDetailResponse.value)
  }
}