package com.future.tailormade_search.feature.search.viewModel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade_search.core.repository.SearchRepository

class SearchViewModel @ViewModelInject constructor(
  private val searchRepository: SearchRepository,
  @Assisted private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

  override fun getLogName(): String =
    "com.future.tailormade_search.feature.search.viewModel.SearchViewModel"

  private var _listOfDesigns = MutableLiveData<ArrayList<String>>()
  val listOfDesigns: LiveData<ArrayList<String>>
    get() = _listOfDesigns

  private var _listOfTailors = MutableLiveData<ArrayList<String>>()
  val listOfTailors: LiveData<ArrayList<String>>
    get() = _listOfTailors
}