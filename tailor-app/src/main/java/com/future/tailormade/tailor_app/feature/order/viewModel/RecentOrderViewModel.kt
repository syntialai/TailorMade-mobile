package com.future.tailormade.tailor_app.feature.order.viewModel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.future.tailormade.base.repository.AuthSharedPrefRepository
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.config.Constants
import com.future.tailormade.tailor_app.core.model.enums.OrderStatus
import com.future.tailormade.tailor_app.core.model.ui.order.OrderUiModel
import com.future.tailormade.tailor_app.core.repository.OrderRepository
import com.future.tailormade.util.extension.onError
import kotlinx.coroutines.flow.collectLatest

class RecentOrderViewModel @ViewModelInject constructor(
    private val orderRepository: OrderRepository,
    private val authSharedPrefRepository: AuthSharedPrefRepository,
    @Assisted private val savedStateHandle: SavedStateHandle) : BaseViewModel() {

  companion object {
    private const val ACCEPTED_ORDERS = "ACCEPTED_ORDERS"
    private const val REJECTED_ORDERS = "REJECTED_ORDERS"
  }

  override fun getLogName() =
      "com.future.tailormade.tailor_app.feature.order.viewModel.RecentOrderViewModel"

  private var _acceptedOrders = MutableLiveData<ArrayList<OrderUiModel>>()
  val acceptedOrders: LiveData<ArrayList<OrderUiModel>>
    get() = _acceptedOrders

  private var _rejectedOrders = MutableLiveData<ArrayList<OrderUiModel>>()
  val rejectedOrders: LiveData<ArrayList<OrderUiModel>>
    get() = _rejectedOrders

//  init {
//    _acceptedOrders = savedStateHandle.getLiveData(ACCEPTED_ORDERS)
//    _rejectedOrders = savedStateHandle.getLiveData(REJECTED_ORDERS)
//  }

  fun fetchAcceptedOrders() {
    fetchRecentOrders(OrderStatus.Accepted.name)
  }

  fun fetchRejectedOrders() {
    fetchRecentOrders(OrderStatus.Rejected.name)
  }

  private fun fetchRecentOrders(orderStatus: String) {
    launchViewModelScope {
      authSharedPrefRepository.userId?.let { tailorId ->
        orderRepository.getOrders(tailorId, orderStatus, page, itemPerPage).onError {
          setErrorMessage(Constants.FAILED_TO_FETCH_RECENT_ORDER)
        }.collectLatest {
          addToList(it, if (orderStatus == Constants.STATUS_ACCEPTED) {
            _acceptedOrders
          } else {
            _rejectedOrders
          })
        }
      }
    }
  }
}