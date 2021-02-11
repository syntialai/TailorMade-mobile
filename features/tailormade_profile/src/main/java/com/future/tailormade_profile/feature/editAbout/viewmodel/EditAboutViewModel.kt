package com.future.tailormade_profile.feature.editAbout.viewmodel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.future.tailormade.base.repository.AuthSharedPrefRepository
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.config.Constants
import com.future.tailormade.util.extension.onError
import com.future.tailormade_profile.core.model.entity.Education
import com.future.tailormade_profile.core.model.entity.Occupation
import com.future.tailormade_profile.core.model.request.UpdateProfileAboutRequest
import com.future.tailormade_profile.core.model.response.ProfileAboutResponse
import com.future.tailormade_profile.core.repository.ProfileRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart

class EditAboutViewModel @ViewModelInject constructor(
    private val profileRepository: ProfileRepository,
    private val authSharedPrefRepository: AuthSharedPrefRepository,
    @Assisted private val savedStateHandle: SavedStateHandle) : BaseViewModel() {

  companion object {
    private const val PROFILE_ABOUT = "PROFILE_ABOUT"
  }

  override fun getLogName(): String =
      "com.future.tailormade_profile.feature.editAbout.viewmodel.EditAboutViewModel"

  private var _profileAbout = MutableLiveData<ProfileAboutResponse>()
  val profileAbout: LiveData<ProfileAboutResponse>
    get() = _profileAbout

  private var _isUpdated = MutableLiveData<Boolean>()
  val isUpdated: LiveData<Boolean>
    get() = _isUpdated

//  init {
//    _profileAbout = savedStateHandle.getLiveData(PROFILE_ABOUT)
//  }

  @ExperimentalCoroutinesApi
  fun updateProfileAbout(occupation: Occupation, education: Education) {
    val request = UpdateProfileAboutRequest(occupation, education)
    launchViewModelScope {
      authSharedPrefRepository.userId?.let { id ->
        profileRepository.updateProfileAbout(id, request).onStart {
          setStartLoading()
        }.onError {
          setFinishLoading()
          setErrorMessage(Constants.FAILED_TO_UPDATE_PROFILE)
        }.collectLatest {
          setFinishLoading()
          _profileAbout.value = it
          _isUpdated.value = true
        }
      }
    }
  }
}