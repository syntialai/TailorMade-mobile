package com.future.tailormade_profile.feature.editProfile.viewModel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.future.tailormade.base.repository.AuthSharedPrefRepository
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.config.Constants
import com.future.tailormade.util.extension.flowOnIOwithLoadingDialog
import com.future.tailormade.util.extension.onError
import com.future.tailormade_profile.core.model.request.UpdateProfileRequest
import com.future.tailormade_profile.core.model.response.ProfileInfoResponse
import com.future.tailormade_profile.core.repository.ProfileRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart

class EditProfileViewModel @ViewModelInject constructor(
    private val profileRepository: ProfileRepository,
    private val authSharedPrefRepository: AuthSharedPrefRepository,
    @Assisted private val savedStateHandle: SavedStateHandle) :
    BaseViewModel() {

  companion object {
    private const val PROFILE_INFO = "PROFILE_INFO"
  }

  override fun getLogName(): String =
      "com.future.tailormade_profile.feature.editProfile.viewModel.EditProfileViewModel"

  private var _profileInfo = MutableLiveData<ProfileInfoResponse>()
  val profileInfo: LiveData<ProfileInfoResponse>
    get() = _profileInfo

  private var _listOfLocations = MutableLiveData<List<String>>()
  val listOfLocations: LiveData<List<String>>
    get() = _listOfLocations

  init {
    _profileInfo = savedStateHandle.getLiveData(PROFILE_INFO)
    getBasicInfo()
  }

  private fun getBasicInfo() {
    launchViewModelScope {
      authSharedPrefRepository.userId?.let { id ->
        profileRepository.getProfileInfo(id).onStart {
          setStartLoading()
        }.onError {
          setFinishLoading()
          setErrorMessage(Constants.FAILED_TO_GET_PROFILE_INFO)
        }.collectLatest { data ->
          setFinishLoading()
          _profileInfo.value = data.response
        }
      }
    }
  }

  @ExperimentalCoroutinesApi
  @InternalCoroutinesApi
  fun updateBasicInfo(name: String, birthDate: Long, phoneNumber: String?, location: String?) {
    val request = UpdateProfileRequest(name, birthDate, phoneNumber.orEmpty(),
        location.orEmpty())
    launchViewModelScope {
      authSharedPrefRepository.userId?.let { id ->
        profileRepository.updateProfileInfo(id, request).onStart {
          setStartLoading()
        }.onError {
          setFinishLoading()
          setErrorMessage(Constants.FAILED_TO_UPDATE_PROFILE)
        }.collectLatest { response ->
          setFinishLoading()
          _profileInfo.value = response
        }
      }
    }
  }

  @ExperimentalCoroutinesApi
  fun updateLocations(query: String) {
    launchViewModelScope {
      profileRepository.searchLocation(query).onError {
        setErrorMessage(it.message.orEmpty())
      }.collectLatest {
        val response = it.map { item ->
          item.display_name.orEmpty()
        }
        _listOfLocations.value = response
      }
    }
  }
}