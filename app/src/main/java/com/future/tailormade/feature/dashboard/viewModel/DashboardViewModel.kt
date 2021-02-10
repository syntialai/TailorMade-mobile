package com.future.tailormade.feature.dashboard.viewModel

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.config.Constants
import com.future.tailormade.core.model.ui.dashboard.DashboardTailorUiModel
import com.future.tailormade.core.repository.DashboardRepository
import com.future.tailormade.util.extension.onError
import com.future.tailormade.util.extension.orEmptyList
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
  fun fetchDashboardTailors() {
    launchViewModelScope {
      dashboardRepository.getDashboardTailors(page, itemPerPage).onError {
        setErrorMessage(Constants.generateFailedFetchError("dashboard"))
      }.collectLatest {
        addToList(it, _tailors)
      }
    }
  }

  @ExperimentalCoroutinesApi
  override fun fetchMore() {
    super.fetchMore()
    fetchDashboardTailors()
  }

  @ExperimentalCoroutinesApi
  override fun refreshFetch() {
    super.refreshFetch()
    fetchDashboardTailors()
  }
}