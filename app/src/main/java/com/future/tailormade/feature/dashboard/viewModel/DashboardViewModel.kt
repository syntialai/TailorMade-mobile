package com.future.tailormade.feature.dashboard.viewModel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.config.Constants
import com.future.tailormade.core.model.response.dashboard.DashboardTailorResponse
import com.future.tailormade.core.repository.DashboardRepository
import com.future.tailormade.util.extension.onError
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart

class DashboardViewModel @ViewModelInject constructor(
    private val dashboardRepository: DashboardRepository) : BaseViewModel() {

  override fun getLogName() = "com.future.tailormade.feature.dashboard.viewModel.DashboardViewModel"

  private var _tailors = MutableLiveData<ArrayList<DashboardTailorResponse>>()
  val tailors: LiveData<ArrayList<DashboardTailorResponse>>
    get() = _tailors

  @ExperimentalCoroutinesApi
  fun fetchDashboardTailors(lat: Double, lon: Double) {
    launchViewModelScope {
      dashboardRepository.getDashboardTailors(lat, lon, page, itemPerPage).onError {
        setErrorMessage(Constants.generateFailedFetchError("dashboard"))
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

  @ExperimentalCoroutinesApi
  override fun fetchMore() {
    super.fetchMore()
    // TODO: Uncomment and Change this position after LocationManager done
//    fetchDashboardTailors(10.0, 10.0)
  }

  @ExperimentalCoroutinesApi
  override fun refreshFetch() {
    super.refreshFetch()
    // TODO: Uncomment and Change this position after LocationManager done
//    fetchDashboardTailors(10.0, 10.0)
  }

  private fun addToList(list: ArrayList<DashboardTailorResponse>, update: Boolean) {
    if (update.not()) {
      _tailors.value?.clear()
    }
    _tailors.value?.addAll(list)
  }
}