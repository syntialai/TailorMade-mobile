package com.future.tailormade.tailor_app.feature.dashboard.viewModel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.config.Constants
import com.future.tailormade.tailor_app.core.model.ui.DashboardDesignUiModel
import com.future.tailormade.tailor_app.core.repository.DashboardRepository
import com.future.tailormade.util.extension.onError
import com.future.tailormade_auth.core.repository.impl.AuthSharedPrefRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart

class DashboardViewModel @ViewModelInject constructor(
    private val dashboardRepository: DashboardRepository,
    private val authSharedPrefRepository: AuthSharedPrefRepository,
    @Assisted private val savedStateHandle: SavedStateHandle) : BaseViewModel() {

  companion object {
    private const val DESIGNS = "DESIGNS"
  }

  override fun getLogName() =
      "com.future.tailormade.feature.dashboard.viewModel.DashboardViewModel.${Constants.TAILOR}"

  private var _designs: MutableLiveData<ArrayList<DashboardDesignUiModel>>
  val designs: LiveData<ArrayList<DashboardDesignUiModel>>
    get() = _designs

  init {
    _designs = savedStateHandle.getLiveData(DESIGNS, arrayListOf())
  }

  @ExperimentalCoroutinesApi
  fun fetchTailorDesigns() {
    launchViewModelScope {
      authSharedPrefRepository.userId?.let { id ->
        dashboardRepository.getDashboardDesigns(id, page, itemPerPage).onStart {
          setStartLoading()
        }.onError {
          setFinishLoading()
          setErrorMessage("Failed to fetch your designs data.")
        }.collectLatest {
          _designs.value = it
          savedStateHandle.set(DESIGNS, _designs.value)
        }
      }
    }
  }

  @ExperimentalCoroutinesApi
  override fun fetchMore() {
    super.fetchMore()
    fetchTailorDesigns()
  }

  @ExperimentalCoroutinesApi
  override fun refreshFetch() {
    super.refreshFetch()
    fetchTailorDesigns()
  }
}