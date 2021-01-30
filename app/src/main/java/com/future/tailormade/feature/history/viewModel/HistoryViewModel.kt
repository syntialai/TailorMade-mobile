package com.future.tailormade.feature.history.viewModel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.future.tailormade.base.repository.AuthSharedPrefRepository
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.config.Constants
import com.future.tailormade.core.model.ui.history.OrderUiModel
import com.future.tailormade.core.repository.OrderRepository
import com.future.tailormade.util.extension.onError
import com.future.tailormade.util.extension.orEmptyList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart

class HistoryViewModel @ViewModelInject constructor(private val orderRepository: OrderRepository,
    private var authSharedPrefRepository: AuthSharedPrefRepository) : BaseViewModel() {

  override fun getLogName(): String = "com.future.tailormade.feature.history.viewModel.HistoryViewModel"

  private var _orders = MutableLiveData<ArrayList<OrderUiModel>>()
  val orders: LiveData<ArrayList<OrderUiModel>>
    get() = _orders

  @ExperimentalCoroutinesApi
  fun fetchHistory() {
    launchViewModelScope {
      authSharedPrefRepository.userId?.let { userId ->
        orderRepository.getOrders(userId, page, itemPerPage).onError {
          setErrorMessage(Constants.generateFailedFetchError("history"))
        }.collectLatest { orders ->
          addToList(orders)
        }
      }
    }
  }

  @ExperimentalCoroutinesApi
  override fun fetchMore() {
    super.fetchMore()
    fetchHistory()
  }

  @ExperimentalCoroutinesApi
  override fun refreshFetch() {
    super.refreshFetch()
    fetchHistory()
  }

  private fun addToList(list: ArrayList<OrderUiModel>) {
    val orders = arrayListOf<OrderUiModel>()
    if (isFirstPage().not()) {
      orders.addAll(_orders.value.orEmptyList())
    }
    orders.addAll(list)
    _orders.value = orders
  }
}