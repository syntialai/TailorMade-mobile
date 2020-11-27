package com.future.tailormade_profile.feature.editProfile.viewModel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.config.Constants
import com.future.tailormade.util.extension.flowOnIOwithLoadingDialog
import com.future.tailormade.util.extension.onError
import com.future.tailormade_auth.core.repository.impl.AuthSharedPrefRepository
import com.future.tailormade_profile.core.model.request.UpdateProfileRequest
import com.future.tailormade_profile.core.model.response.ProfileInfoResponse
import com.future.tailormade_profile.core.repository.ProfileRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect

class EditProfileViewModel @ViewModelInject constructor(
    private val profileRepository: ProfileRepository,
    private val authSharedPrefRepository: AuthSharedPrefRepository,
    @Assisted private val savedStateHandle: SavedStateHandle) :
    BaseViewModel() {

  override fun getLogName(): String =
      "com.future.tailormade_profile.feature.editProfile.viewModel.EditProfileViewModel"

  private var _profileInfo = MutableLiveData<ProfileInfoResponse>()
  val profileInfo: LiveData<ProfileInfoResponse>
    get() = _profileInfo

  private var _listOfLocations = MutableLiveData<List<String>>()
  val listOfLocations: LiveData<List<String>>
    get() = _listOfLocations

  init {
    getBasicInfo()
  }

  private fun getBasicInfo() {
    launchViewModelScope {
      authSharedPrefRepository.userId?.let { id ->
        profileRepository.getProfileInfo(id).onError {
          _errorMessage.postValue(Constants.FAILED_TO_GET_PROFILE_INFO)
        }.collect { response ->
          response.data?.let {
            _profileInfo.postValue(it)
          }
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
        profileRepository.updateProfileInfo(id, request).flowOnIOwithLoadingDialog(
            this).onError {
          _errorMessage.postValue(Constants.FAILED_TO_UPDATE_PROFILE)
          appLogger.logOnError(Constants.FAILED_TO_UPDATE_PROFILE, it)
        }.collect { response ->
          response.data?.let {
            _profileInfo.postValue(it)
          }
        }
      }
    }
  }

  @ExperimentalCoroutinesApi
  fun updateLocations(query: String) {
    launchViewModelScope {
      profileRepository.searchLocation(query).onError {
        _errorMessage.value = it.toString()
      }.collect {
        val response = it.map { item ->
          appLogger.logOnEvent(it.toString())
          item.display_name.orEmpty()
        }
        _listOfLocations.value = response
      }
    }
  }
}