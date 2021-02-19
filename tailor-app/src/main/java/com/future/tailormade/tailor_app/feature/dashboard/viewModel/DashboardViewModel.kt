package com.future.tailormade.tailor_app.feature.dashboard.viewModel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.future.tailormade.base.repository.AuthSharedPrefRepository
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.config.Constants
import com.future.tailormade.tailor_app.core.model.ui.dashboard.DashboardDesignUiModel
import com.future.tailormade.tailor_app.core.repository.DashboardRepository
import com.future.tailormade.util.extension.onError
import com.future.tailormade.util.extension.orFalse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest

class DashboardViewModel @ViewModelInject constructor(
    private val dashboardRepository: DashboardRepository,
    private val authSharedPrefRepository: AuthSharedPrefRepository,
    @Assisted private val savedStateHandle: SavedStateHandle) : BaseViewModel() {

  companion object {
    private const val DESIGNS = "DESIGNS"
    private const val SELECTED_DESIGNS = "SELECTED_DESIGNS"
  }

  override fun getLogName() =
      "com.future.tailormade.tailor_app.feature.dashboard.viewModel.DashboardViewModel"

  private var _designs = MutableLiveData<ArrayList<DashboardDesignUiModel>>()
  val designs: LiveData<ArrayList<DashboardDesignUiModel>>
    get() = _designs

  private var _hasBeenDeleted = MutableLiveData<Boolean>()
  val hasBeenDeleted: LiveData<Boolean>
    get() = _hasBeenDeleted

//  init {
//    _designs = savedStateHandle.getLiveData(DESIGNS)
//    _selectedDesigns = savedStateHandle.getLiveData(SELECTED_DESIGNS)
//  }

  @ExperimentalCoroutinesApi
  fun fetchTailorDesigns() {
    launchViewModelScope {
      authSharedPrefRepository.userId?.let { id ->
        dashboardRepository.getDashboardDesigns(id, page, itemPerPage).onError {
          setErrorMessage(Constants.FAILED_TO_GET_YOUR_DESIGN)
        }.collectLatest {
          addToList(it, _designs)
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

  fun deleteDesign(id: String) {
    launchViewModelScope {
      authSharedPrefRepository.userId?.let { tailorId ->
        dashboardRepository.deleteDashboardDesign(tailorId, id).onError {
          setErrorMessage(Constants.FAILED_TO_DELETE_DESIGN)
          _hasBeenDeleted.value = false
        }.collect {
          _hasBeenDeleted.value = true
        }
      }
    }
  }
}