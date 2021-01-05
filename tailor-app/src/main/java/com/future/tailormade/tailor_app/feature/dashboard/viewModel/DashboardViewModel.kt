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
import com.future.tailormade.util.extension.orFalse
import com.future.tailormade.util.extension.orZero
import com.future.tailormade_auth.core.repository.impl.AuthSharedPrefRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart

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

  private var _designs: MutableLiveData<ArrayList<DashboardDesignUiModel>>
  val designs: LiveData<ArrayList<DashboardDesignUiModel>>
    get() = _designs

  private var _selectedDesigns: MutableLiveData<ArrayList<String>>
  val selectedDesigns: LiveData<ArrayList<String>>
    get() = _selectedDesigns

  private var deletedItem: Int = 0

  init {
    _designs = savedStateHandle.getLiveData(DESIGNS, arrayListOf())
    _selectedDesigns = savedStateHandle.getLiveData(SELECTED_DESIGNS, arrayListOf())
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

  fun deleteDesigns() {
    launchViewModelScope {
      authSharedPrefRepository.userId?.let { userId ->
        _selectedDesigns.value?.forEach {
          deleteDesign(userId, it)
        }
        _selectedDesigns.value?.clear()
      }
    }
  }

  fun selectAllDesigns(selected: Boolean) {
    val designs = _designs.value?.map { it.id } as ArrayList
    _selectedDesigns.value?.let { selectedDesigns ->
      selectedDesigns.clear()
      if (!selected) {
        selectedDesigns.addAll(designs)
      }
    }
  }

  fun selectDesign(id: String) {
    if (isDesignSelected(id).orFalse()) {
      _selectedDesigns.value?.remove(id)
    } else {
      _selectedDesigns.value?.add(id)
    }
  }

  private suspend fun deleteDesign(tailorId: String, id: String) {
    dashboardRepository.deleteDashboardDesign(tailorId, id).onError {
      setErrorMessage(Constants.generateDeleteErrorMessage("design", id))
    }.collect {
      deletedItem.inc()
    }
  }

  private fun isDesignSelected(id: String) = _selectedDesigns.value?.contains(id)
}