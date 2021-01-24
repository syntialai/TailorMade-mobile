package com.future.tailormade_profile.feature.profile.viewModel

import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.future.tailormade.base.repository.AuthSharedPrefRepository
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.config.Constants
import com.future.tailormade.util.extension.onError
import com.future.tailormade_profile.core.model.ui.ProfileInfoUiModel
import com.future.tailormade_profile.core.repository.ProfileRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart

class ProfileViewModel @ViewModelInject constructor(
    private val profileRepository: ProfileRepository,
    private val authSharedPrefRepository: AuthSharedPrefRepository,
    @Assisted private val savedStateHandle: SavedStateHandle) :
    BaseViewModel() {

  companion object {
    private const val PROFILE_INFO = "profileInfo"
  }

  override fun getLogName() = "com.future.tailormade_profile.feature.profile.viewModel.ProfileViewModel"

  private var _profileInfoUiModel = MutableLiveData<ProfileInfoUiModel>()
  val profileInfoUiModel: LiveData<ProfileInfoUiModel>
    get() = _profileInfoUiModel

  init {
    _profileInfoUiModel = savedStateHandle.getLiveData(PROFILE_INFO)
  }

  @ExperimentalCoroutinesApi
  @InternalCoroutinesApi
  fun fetchProfileInfo() {
    launchViewModelScope {
      authSharedPrefRepository.userId?.let { id ->
        profileRepository.getProfileInfo(id).onStart {
          setStartLoading()
        }.onError {
          setFinishLoading()
          Log.d(PROFILE_INFO, it.message, it)
          setErrorMessage(Constants.FAILED_TO_GET_PROFILE_INFO)
        }.collectLatest {
          _profileInfoUiModel.value = it.uiModel
        }
      }
    }
  }
}