package com.future.tailormade.feature.dashboard.viewModel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.core.model.response.dashboard.DashboardTailorResponse
import com.future.tailormade.core.repository.DashboardRepository
import com.future.tailormade.util.extension.onError
import kotlinx.coroutines.flow.collect

class DashboardViewModel @ViewModelInject constructor(
    private val dashboardRepository: DashboardRepository,
    @Assisted private val savedStateHandle: SavedStateHandle) :
    BaseViewModel() {

  companion object {
    private const val TAILORS = "tailors"
  }

  override fun getLogName() = "com.future.tailormade.feature.dashboard.viewModel.DashboardViewModel"

  private var page = 1
  private var itemPerPage = 10

  private var _tailors = MutableLiveData<ArrayList<DashboardTailorResponse>>()
  val tailors: LiveData<ArrayList<DashboardTailorResponse>>
    get() = _tailors

  init {
    _tailors = savedStateHandle.getLiveData(TAILORS)
  }

  fun fetchDashboardTailors(lat: Double, lon: Double) {
    launchViewModelScope {
      dashboardRepository.getDashboardTailors(lat, lon, page, itemPerPage).onError {
        _errorMessage.value = "Failed to fetch dashboard data"
      }.collect { response ->
        response.data?.let {
          _tailors.value = it as ArrayList
          savedStateHandle.set(TAILORS, _tailors)
        }
      }
    }
  }
}