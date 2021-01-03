package com.future.tailormade.feature.dashboard.viewModel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.config.Constants
import com.future.tailormade.core.model.ui.dashboard.DashboardTailorUiModel
import com.future.tailormade.core.repository.DashboardRepository
import com.future.tailormade.util.extension.onError
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart

class DashboardViewModel @ViewModelInject constructor(
    private val dashboardRepository: DashboardRepository) : BaseViewModel() {

  override fun getLogName() = "com.future.tailormade.feature.dashboard.viewModel.DashboardViewModel"

  private var _tailors = MutableLiveData<ArrayList<DashboardTailorUiModel>>()
  val tailors: LiveData<ArrayList<DashboardTailorUiModel>>
    get() = _tailors

  @ExperimentalCoroutinesApi
  fun fetchDashboardTailors(lat: Double, lon: Double) {
    launchViewModelScope {
      dashboardRepository.getDashboardTailors(lat, lon, page, itemPerPage).onError {
        setErrorMessage(Constants.generateFailedFetchError("dashboard"))
        setFinishLoading()
      }.onStart {
        setStartLoading()
      }.collectLatest {
        addToList(it, isFirstPage())
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

  private fun addToList(list: ArrayList<DashboardTailorUiModel>, update: Boolean) {
    if (update.not()) {
      _tailors.value?.clear()
    }
    _tailors.value = list
  }
}