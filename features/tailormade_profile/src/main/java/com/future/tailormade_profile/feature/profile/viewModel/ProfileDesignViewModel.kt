package com.future.tailormade_profile.feature.profile.viewModel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.future.tailormade.base.repository.AuthSharedPrefRepository
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.config.Constants
import com.future.tailormade.util.extension.onError
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
          setErrorMessage(Constants.generateFailedFetchError("designs"))
        }.collectLatest {
          addToList(it, _images)
          setFinishLoading()
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
}