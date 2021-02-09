package com.future.tailormade_search.feature.search.viewModel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.config.Constants
import com.future.tailormade.util.extension.onError
import com.future.tailormade.util.extension.orEmptyList
import com.future.tailormade.util.extension.orZero
import com.future.tailormade_search.core.model.response.SearchDesignResponse
import com.future.tailormade_search.core.model.response.SearchTailorResponse
import com.future.tailormade_search.core.repository.SearchRepository
import kotlinx.coroutines.flow.collect

class SearchViewModel @ViewModelInject constructor(private val searchRepository: SearchRepository,
    @Assisted private val savedStateHandle: SavedStateHandle) : BaseViewModel() {

  companion object {
    private const val LIST_OF_DESIGNS = "LIST_OF_DESIGNS"
    private const val LIST_OF_TAILORS = "LIST_OF_TAILORS"
  }

  override fun getLogName(): String =
      "com.future.tailormade_search.feature.search.viewModel.SearchViewModel"

  private var _listOfDesigns = MutableLiveData<List<SearchDesignResponse>>()
  val listOfDesigns: LiveData<List<SearchDesignResponse>>
    get() = _listOfDesigns

  private var _listOfTailors = MutableLiveData<List<SearchTailorResponse>>()
  val listOfTailors: LiveData<List<SearchTailorResponse>>
    get() = _listOfTailors

  private var _designCount = MutableLiveData<Int>()
  private var _tailorCount = MutableLiveData<Int>()

  val searchResultCount = MediatorLiveData<Long>().apply {
    value = -1L
  }

  init {
//    _listOfDesigns = savedStateHandle.getLiveData(LIST_OF_DESIGNS)
//    _listOfTailors = savedStateHandle.getLiveData(LIST_OF_TAILORS)

    searchResultCount.addSource(_designCount) {
      if (it > searchResultCount.value.orZero()) {
        searchResultCount.value = it.toLong()
      }
    }

    searchResultCount.addSource(_tailorCount) {
      if (it > searchResultCount.value.orZero()) {
        searchResultCount.value = it.toLong()
      }
    }
  }

  fun searchDesign(query: String) {
    launchViewModelScope {
      searchRepository.searchDesign(query, page, itemPerPage).onError {
        setErrorMessage(Constants.generateFailedFetchError("design"))
      }.collect {
        _designCount.value = it.paging?.totalItem.orZero()
        _listOfDesigns.value = it.data.orEmptyList()
      }
    }
  }

  fun searchTailor(query: String) {
    launchViewModelScope {
      searchRepository.searchTailor(query, page, itemPerPage).onError {
        setErrorMessage(Constants.generateFailedFetchError("tailor"))
      }.collect {
        _tailorCount.value = it.paging?.totalItem.orZero()
        _listOfTailors.value = it.data.orEmptyList()
      }
    }
  }

  fun isQueryValid(query: String) = query.isNotBlank() && query.length >= 3
}