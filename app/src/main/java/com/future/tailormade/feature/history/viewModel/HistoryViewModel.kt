package com.future.tailormade.feature.history.viewModel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.config.Constants
import com.future.tailormade.core.model.response.history.OrderResponse
import com.future.tailormade.core.repository.OrderRepository
import com.future.tailormade.util.extension.onError
import com.future.tailormade_auth.core.repository.impl.AuthSharedPrefRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart

class HistoryViewModel @ViewModelInject constructor(private val orderRepository: OrderRepository,
    private var authSharedPrefRepository: AuthSharedPrefRepository) : BaseViewModel() {

  override fun getLogName(): String = "com.future.tailormade.feature.history.viewModel.HistoryViewModel"

  private var _orders = MutableLiveData<ArrayList<OrderResponse>>()
  val orders: LiveData<ArrayList<OrderResponse>>
    get() = _orders

  @ExperimentalCoroutinesApi
  fun fetchHistory() {
    launchViewModelScope {
      authSharedPrefRepository.userId?.let { userId ->
        orderRepository.getOrders(userId, page, itemPerPage).onError {
          setErrorMessage(Constants.generateFailedFetchError("history"))
          setFinishLoading()
        }.onStart {
          setStartLoading()
        }.collectLatest { response ->
          response.data?.let {
            addToList(it as ArrayList, isFirstPage())
          }
          setFinishLoading()
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

  private fun addToList(list: ArrayList<OrderResponse>, update: Boolean) {
    if (update.not()) {
      _orders.value?.clear()
    }
    _orders.value?.addAll(list)
  }
}