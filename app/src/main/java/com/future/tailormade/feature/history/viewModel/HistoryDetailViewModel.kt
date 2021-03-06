package com.future.tailormade.feature.history.viewModel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.future.tailormade.base.repository.AuthSharedPrefRepository
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.config.Constants
import com.future.tailormade.core.model.ui.history.OrderDetailUiModel
import com.future.tailormade.core.repository.OrderRepository
import com.future.tailormade.util.extension.onError
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest

class HistoryDetailViewModel @ViewModelInject constructor(
    private val orderRepository: OrderRepository,
    private val authSharedPrefRepository: AuthSharedPrefRepository,
    @Assisted private val savedStateHandle: SavedStateHandle) : BaseViewModel() {

  companion object {
    private const val ORDER_DETAIL = "ORDER_DETAIL"
  }

  override fun getLogName() = "com.future.tailormade.feature.history.viewModel.HistoryDetailViewModel"

  private var _orderDetailUiModel = MutableLiveData<OrderDetailUiModel>()
  val orderDetailUiModel: LiveData<OrderDetailUiModel>
    get() = _orderDetailUiModel

//  init {
//    _orderDetailUiModel = savedStateHandle.getLiveData(ORDER_DETAIL)
//  }

  @ExperimentalCoroutinesApi
  fun fetchHistoryDetails(id: String) {
    launchViewModelScope {
      authSharedPrefRepository.userId?.let { userId ->
        orderRepository.getOrderDetail(userId, id).onError {
          setErrorMessage(Constants.generateFailedFetchError("history with id $id"))
        }.collectLatest { orderDetail ->
          _orderDetailUiModel.value = orderDetail
        }
      }
    }
  }
}