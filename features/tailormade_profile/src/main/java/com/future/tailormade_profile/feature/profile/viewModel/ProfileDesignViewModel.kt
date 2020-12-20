package com.future.tailormade_profile.feature.profile.viewModel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.util.extension.onError
import com.future.tailormade_auth.core.repository.impl.AuthSharedPrefRepository
import com.future.tailormade_profile.core.model.response.ProfileDesignResponse
import com.future.tailormade_profile.core.repository.ProfileRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart

class ProfileDesignViewModel @ViewModelInject constructor(
    private val profileRepository: ProfileRepository,
    private val authSharedPrefRepository: AuthSharedPrefRepository) : BaseViewModel() {

  override fun getLogName() =
      "com.future.tailormade_profile.feature.profile.viewModel.ProfileDesignViewModel"

  private var _images = MutableLiveData<ArrayList<ProfileDesignResponse>>()
  val images: LiveData<ArrayList<ProfileDesignResponse>>
    get() = _images

  @ExperimentalCoroutinesApi
  fun fetchImages() {
    launchViewModelScope {
      authSharedPrefRepository.userId?.let { id ->
        profileRepository.getProfileDesigns(id, page, itemPerPage).onStart {
          setStartLoading()
        }.onError {
          setFinishLoading()
          setErrorMessage("Failed to get designs. Please try again.")
        }.collectLatest { response ->
          response.data?.let {
            addToList(it as ArrayList, isFirstPage())
            setFinishLoading()
          }
        }
      }
    }
  }

  @ExperimentalCoroutinesApi
  override fun fetchMore() {
    super.fetchMore()
    fetchImages()
  }

  @ExperimentalCoroutinesApi
  override fun refreshFetch() {
    super.refreshFetch()
    fetchImages()
  }

  private fun addToList(list: ArrayList<ProfileDesignResponse>, update: Boolean) {
    if (update.not()) {
      _images.value?.clear()
    }
    _images.value?.addAll(list)
  }
}