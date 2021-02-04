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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest

class IncomingOrderViewModel @ViewModelInject constructor(
    private val orderRepository: OrderRepository,
    private val authSharedPrefRepository: AuthSharedPrefRepository,
    @Assisted private val savedStateHandle: SavedStateHandle) : BaseViewModel() {

  companion object {
    private const val INCOMING_ORDERS = "INCOMING_ORDERS"
  }

  override fun getLogName() =
      "com.future.tailormade.tailor_app.feature.order.viewModel.IncomingOrderViewModel"

  private var _incomingOrders: MutableLiveData<ArrayList<OrderUiModel>>
  val incomingOrders: LiveData<ArrayList<OrderUiModel>>
    get() = _incomingOrders

  private var _hasOrderResponded = MutableLiveData<Pair<String, Boolean>>()
  val hasOrderResponded: LiveData<Pair<String, Boolean>>
    get() = _hasOrderResponded

  init {
    _incomingOrders = savedStateHandle.getLiveData(INCOMING_ORDERS)
  }

  fun fetchIncomingOrders() {
    _hasOrderResponded.value = null
    launchViewModelScope {
      authSharedPrefRepository.userId?.let { tailorId ->
        orderRepository.getOrders(tailorId, OrderStatus.Incoming.name, page, itemPerPage).onError {
          setErrorMessage(Constants.FAILED_TO_FETCH_INCOMING_ORDER)
        }.collectLatest {
          addToList(it, _incomingOrders)
        }
      }
    }
  }

  @ExperimentalCoroutinesApi
  override fun fetchMore() {
    super.fetchMore()
    fetchIncomingOrders()
  }

  fun acceptOrder(id: String) {
    launchViewModelScope {
      authSharedPrefRepository.userId?.let { tailorId ->
        orderRepository.acceptOrder(tailorId, id).onError {
          setErrorMessage(Constants.FAILED_TO_ACCEPT_ORDER)
          setHasResponded(Constants.STATUS_ACCEPTED, false)
        }.collect {
          setHasResponded(Constants.STATUS_ACCEPTED, true)
        }
      }
    }
  }

  fun rejectOrder(id: String) {
    launchViewModelScope {
      authSharedPrefRepository.userId?.let { tailorId ->
        orderRepository.rejectOrder(tailorId, id).onError {
          setErrorMessage(Constants.FAILED_TO_REJECT_ORDER)
          setHasResponded(Constants.STATUS_REJECTED, false)
        }.collect {
          setHasResponded(Constants.STATUS_REJECTED, true)
        }
      }
    }
  }

  private fun setHasResponded(type: String, value: Boolean) {
    _hasOrderResponded.value = Pair(type, value)
  }
}