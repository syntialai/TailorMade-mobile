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

  private var _images: MutableLiveData<ArrayList<ProfileDesignResponse>>
  val images: LiveData<ArrayList<ProfileDesignResponse>>
    get() = _images

  init {
    _images = savedStateHandle.getLiveData(IMAGES)
  }

  @ExperimentalCoroutinesApi
  fun fetchImages(id: String) {
    _id = id
    launchViewModelScope {
      profileRepository.getProfileDesigns(id, page, itemPerPage).onError {
        setErrorMessage(Constants.generateFailedFetchError("tailor designs"))
      }.collectLatest {
//        Log.d(IMAGES, it.toString())
        addToList(it, _images)
//        Log.d(IMAGES, _images.value.toString())
      }
    }
  }

  @ExperimentalCoroutinesApi
  override fun fetchMore() {
    super.fetchMore()
    _id?.let {
      fetchImages(it)
    }
  }

  @ExperimentalCoroutinesApi
  override fun refreshFetch() {
    super.refreshFetch()
    _id?.let {
      fetchImages(it)
    }
  }
}