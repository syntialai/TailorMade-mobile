package com.future.tailormade.tailor_app.feature.orderDetail.viewModel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.config.Constants
import com.future.tailormade.tailor_app.core.model.ui.orderDetail.OrderDetailUiModel
import com.future.tailormade.tailor_app.core.repository.OrderRepository
import com.future.tailormade.util.extension.onError
import com.future.tailormade_auth.core.repository.impl.AuthSharedPrefRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart

class OrderDetailViewModel @ViewModelInject constructor(
    private val orderRepository: OrderRepository,
    private val authSharedPrefRepository: AuthSharedPrefRepository,
    @Assisted private val savedStateHandle: SavedStateHandle) : BaseViewModel() {

  override fun getLogName() =
      "com.future.tailormade.tailor_app.feature.orderDetail.viewModel.OrderDetailViewModel"

  companion object {
    private const val ORDER_DETAIL_UI_MODEL = "ORDER_DETAIL_UI_MODEL"
  }

  private var _orderDetailUiModel: MutableLiveData<OrderDetailUiModel>
  val orderDetailUiModel: LiveData<OrderDetailUiModel>
    get() = _orderDetailUiModel

  init {
    _orderDetailUiModel = savedStateHandle.getLiveData(ORDER_DETAIL_UI_MODEL)
  }

  fun fetchOrderDetail(id: String) {
    launchViewModelScope {
      authSharedPrefRepository.userId?.let { tailorId ->
        orderRepository.getOrderDetail(tailorId, id).onStart {
          setStartLoading()
        }.onError {
          setFinishLoading()
          setErrorMessage(Constants.FAILED_TO_FETCH_ORDER_DETAIL)
        }.collectLatest {
          _orderDetailUiModel.value = it
          setFinishLoading()
        }
      }
    }
  }
}