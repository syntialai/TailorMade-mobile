package com.future.tailormade_profile.feature.editAbout.viewmodel

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
import com.future.tailormade_profile.core.model.entity.Education
import com.future.tailormade_profile.core.model.entity.Occupation
import com.future.tailormade_profile.core.model.request.UpdateProfileAboutRequest
import com.future.tailormade_profile.core.model.response.ProfileAboutResponse
import com.future.tailormade_profile.core.repository.ProfileRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect

class EditAboutViewModel @ViewModelInject constructor(
    private val profileRepository: ProfileRepository,
    private val authSharedPrefRepository: AuthSharedPrefRepository,
    @Assisted private val savedStateHandle: SavedStateHandle) :
    BaseViewModel() {

  override fun getLogName(): String =
      "com.future.tailormade_profile.feature.editAbout.viewmodel.EditAboutViewModel"

  private var _profileAbout = MutableLiveData<ProfileAboutResponse>()
  val profileAbout: LiveData<ProfileAboutResponse>
    get() = _profileAbout

  @ExperimentalCoroutinesApi
  fun updateProfileAbout(occupation: Occupation, education: Education) {
    val request = UpdateProfileAboutRequest(occupation, education)
    launchViewModelScope {
      authSharedPrefRepository.userId?.let { id ->
        profileRepository.updateProfileAbout(id, request).flowOnIOwithLoadingDialog(
            this).onError {
          _errorMessage.postValue(Constants.FAILED_TO_UPDATE_PROFILE)
          appLogger.logOnError(Constants.FAILED_TO_UPDATE_PROFILE, it)
        }.collect { response ->
          response.data?.let {
            _profileAbout.value = it
          }
        }
      }
    }
  }
}