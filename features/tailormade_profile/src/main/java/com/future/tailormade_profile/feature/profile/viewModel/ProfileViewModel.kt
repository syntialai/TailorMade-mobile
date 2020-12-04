package com.future.tailormade_profile.feature.profile.viewModel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.config.Constants
import com.future.tailormade.util.extension.onError
import com.future.tailormade_auth.core.repository.impl.AuthSharedPrefRepository
import com.future.tailormade_profile.core.model.response.ProfileAboutResponse
import com.future.tailormade_profile.core.model.response.ProfileInfoResponse
import com.future.tailormade_profile.core.repository.ProfileRepository
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect

class ProfileViewModel @ViewModelInject constructor(
    private val profileRepository: ProfileRepository,
    private val authSharedPrefRepository: AuthSharedPrefRepository,
    @Assisted private val savedStateHandle: SavedStateHandle) :
    BaseViewModel() {

  companion object {
    private const val PROFILE_INFO = "profileInfo"
  }

  override fun getLogName() = "com.future.tailormade_profile.feature.profile.viewModel.ProfileViewModel"

  private var _profileInfoResponse = MutableLiveData<ProfileInfoResponse>()
  val profileInfoResponse: LiveData<ProfileInfoResponse>
    get() = _profileInfoResponse

  init {
    _profileInfoResponse = getProfileInfo()
  }

  @InternalCoroutinesApi
  fun fetchProfileInfo() {
    launchViewModelScope {
      authSharedPrefRepository.userId?.let { id ->
        profileRepository.getProfileInfo(id).onError {
          _errorMessage.value = Constants.FAILED_TO_GET_PROFILE_INFO
        }.collect { response ->
          response.data?.let {
            setProfileInfo(it)
            _profileInfoResponse.value = it
          }
        }
      }
    }
  }

  fun isUserProfile(id: String) = authSharedPrefRepository.userId == id

  private fun getProfileInfo() = savedStateHandle.getLiveData<ProfileInfoResponse>(PROFILE_INFO)

  private fun setProfileInfo(profileInfoResponse: ProfileInfoResponse) {
    savedStateHandle.set(PROFILE_INFO, profileInfoResponse)
  }
}