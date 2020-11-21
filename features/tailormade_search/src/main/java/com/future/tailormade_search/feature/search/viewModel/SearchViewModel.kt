package com.future.tailormade_search.feature.search.viewModel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.util.extension.onError
import com.future.tailormade.util.extension.orEmptyList
import com.future.tailormade_search.core.model.response.SearchDesignResponse
import com.future.tailormade_search.core.model.response.SearchTailorResponse
import com.future.tailormade_search.core.repository.SearchRepository
import kotlinx.coroutines.flow.collect

class SearchViewModel @ViewModelInject constructor(
  private val searchRepository: SearchRepository,
  @Assisted private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

  override fun getLogName(): String =
    "com.future.tailormade_search.feature.search.viewModel.SearchViewModel"

  private var _listOfDesigns = MutableLiveData<List<SearchDesignResponse>>()
  val listOfDesigns: LiveData<List<SearchDesignResponse>>
    get() = _listOfDesigns

  private var _listOfTailors = MutableLiveData<List<SearchTailorResponse>>()
  val listOfTailors: LiveData<List<SearchTailorResponse>>
    get() = _listOfTailors

  fun searchDesign(query: String) {
    launchViewModelScope {
      searchRepository.searchDesign(query).onError {
        appLogger.logOnError("Error to find design:", it)
      }.collect {
        _listOfDesigns.value = it.data.orEmptyList()
      }
    }
  }

  fun searchTailor(query: String) {
    launchViewModelScope {
      searchRepository.searchTailor(query).onError {
        appLogger.logOnError("Error to find tailor:", it)
      }.collect {
        _listOfTailors.value = it.data.orEmptyList()
      }
    }
  }
}