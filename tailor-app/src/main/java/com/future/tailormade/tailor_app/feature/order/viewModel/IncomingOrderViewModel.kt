package com.future.tailormade.tailor_app.feature.order.viewModel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.future.tailormade.base.repository.AuthSharedPrefRepository
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.config.Constants
import com.future.tailormade.tailor_app.core.model.enums.OrderStatus
import com.future.tailormade.tailor_app.core.model.ui.order.OrderUiModel
import com.future.tailormade.tailor_app.core.repository.OrderRepository
import com.future.tailormade.util.extension.onError
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart

class IncomingOrderViewModel @ViewModelInject constructor(
    private val orderRepository: OrderRepository,
    private val authSharedPrefRepository: AuthSharedPrefRepository) : BaseViewModel() {

  override fun getLogName() =
      "com.future.tailormade.tailor_app.feature.order.viewModel.IncomingOrderViewModel"

  private var _incomingOrders = MutableLiveData<ArrayList<OrderUiModel>>()
  val incomingOrders: LiveData<ArrayList<OrderUiModel>>
    get() = _incomingOrders

  private var _hasOrderResponded = MutableLiveData<Boolean>()
  val hasOrderResponded: LiveData<Boolean>
    get() = _hasOrderResponded

  fun fetchIncomingOrders() {
    launchViewModelScope {
      authSharedPrefRepository.userId?.let { tailorId ->
        orderRepository.getOrders(tailorId, OrderStatus.INCOMING.name).onStart {
          setStartLoading()
        }.onError {
          setErrorMessage(Constants.FAILED_TO_FETCH_INCOMING_ORDER)
        }.collectLatest {
          _incomingOrders.value = it
        }
      }
    }
  }

  fun acceptOrder(id: String) {
    launchViewModelScope {
      authSharedPrefRepository.userId?.let { tailorId ->
        orderRepository.acceptOrder(tailorId, id).onError {
          setErrorMessage(Constants.FAILED_TO_ACCEPT_ORDER)
          _hasOrderResponded.value = false
        }.collect {
          _hasOrderResponded.value = true
        }
      }
    }
  }

  fun rejectOrder(id: String) {
    launchViewModelScope {
      authSharedPrefRepository.userId?.let { tailorId ->
        orderRepository.rejectOrder(tailorId, id).onError {
          setErrorMessage(Constants.FAILED_TO_REJECT_ORDER)
          _hasOrderResponded.value = false
        }.collect {
          _hasOrderResponded.value = true
        }
      }
    }
  }
}