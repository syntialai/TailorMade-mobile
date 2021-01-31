package com.future.tailormade.tailor_app.feature.order.viewModel

import android.util.Log
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
import com.future.tailormade.util.extension.orEmptyList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest

class RecentOrderViewModel @ViewModelInject constructor(
    private val orderRepository: OrderRepository,
    private val authSharedPrefRepository: AuthSharedPrefRepository) : BaseViewModel() {

  override fun getLogName() =
      "com.future.tailormade.tailor_app.feature.order.viewModel.RecentOrderViewModel"

  private var _orders: ArrayList<OrderUiModel> = arrayListOf()

  private var _recentOrders = MutableLiveData<ArrayList<OrderUiModel>>()
  val recentOrders: LiveData<ArrayList<OrderUiModel>>
    get() = _recentOrders

  fun fetchIncomingOrders() {
    launchViewModelScope {
      authSharedPrefRepository.userId?.let { tailorId ->
        orderRepository.getOrders(tailorId, OrderStatus.Accepted.name, page, itemPerPage).onError {
          setErrorMessage(Constants.FAILED_TO_FETCH_RECENT_ORDER)
          Log.d("RECENT", it.message, it)
        }.collectLatest {
          addToList(it)
          filterAllOrders()
        }
      }
    }
  }

  @ExperimentalCoroutinesApi
  override fun fetchMore() {
    super.fetchMore()
    fetchIncomingOrders()
  }

  fun filterAcceptedOrders() {
    filterOrders(Constants.STATUS_ACCEPTED)
  }

  fun filterAllOrders() {
    _recentOrders.value = _orders
  }

  fun filterRejectedOrders() {
    filterOrders(Constants.STATUS_REJECTED)
  }

  private fun addToList(list: ArrayList<OrderUiModel>) {
    val orders = arrayListOf<OrderUiModel>()
    if (isFirstPage().not()) {
      orders.addAll(_orders.orEmptyList())
    }
    orders.addAll(list)
    _orders = orders
  }

  private fun filterOrders(status: String) {
    val orders = _orders.filter { it.status == status } as ArrayList
    _recentOrders.value = orders
  }
}