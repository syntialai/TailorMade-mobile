package com.future.tailormade_profile.feature.profile.viewModel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.config.Constants
import com.future.tailormade.util.extension.onError
import com.future.tailormade_profile.core.model.response.ProfileDesignResponse
import com.future.tailormade_profile.core.repository.ProfileRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest

class ProfileDesignViewModel @ViewModelInject constructor(
    private val profileRepository: ProfileRepository,
    @Assisted private val savedStateHandle: SavedStateHandle) : BaseViewModel() {

  companion object {
    private const val IMAGES = "IMAGES"
  }

  override fun getLogName() =
      "com.future.tailormade_profile.feature.profile.viewModel.ProfileDesignViewModel"

  private var _id: String? = null

  private var _images = MutableLiveData<ArrayList<ProfileDesignResponse>>()
  val images: LiveData<ArrayList<ProfileDesignResponse>>
    get() = _images

//  init {
//    _images = savedStateHandle.getLiveData(IMAGES)
//  }

  @ExperimentalCoroutinesApi
  fun fetchImages() {
    launchViewModelScope {
      _id?.let { id ->
        profileRepository.getProfileDesigns(id, page, itemPerPage).onError {
          setErrorMessage(Constants.generateFailedFetchError("tailor designs"))
        }.collectLatest {
          addToList(it, _images)
        }
      }
    }
  }

  fun setId(id: String) {
    _id = id
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