package com.future.tailormade_profile.feature.profile.viewModel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.config.Constants
import com.future.tailormade.util.extension.onError
import com.future.tailormade_profile.core.model.ui.ProfileInfoUiModel
import com.future.tailormade_profile.core.repository.ProfileRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart

class TailorProfileViewModel @ViewModelInject constructor(
    private val profileRepository: ProfileRepository,
    @Assisted private val savedStateHandle: SavedStateHandle) : BaseViewModel() {

  companion object {
    private const val TAILOR_PROFILE_INFO = "tailorProfileInfo"
  }

  override fun getLogName() = "com.future.tailormade_profile.feature.profile.viewModel.TailorProfileViewModel"

  private var tailorId: String? = null

  private var _profileInfoUiModel = MutableLiveData<ProfileInfoUiModel>()
  val profileInfoUiModel: LiveData<ProfileInfoUiModel>
    get() = _profileInfoUiModel

  init {
    _profileInfoUiModel = savedStateHandle.getLiveData(TAILOR_PROFILE_INFO)
  }

  @ExperimentalCoroutinesApi
  @InternalCoroutinesApi
  fun fetchTailorProfileInfo() {
    launchViewModelScope {
      tailorId?.let { id ->
        profileRepository.getTailorProfileInfo(id).onStart {
          setStartLoading()
        }.onError {
          setFinishLoading()
          setErrorMessage(Constants.FAILED_TO_GET_PROFILE_INFO)
        }.collectLatest {
          _profileInfoUiModel.value = it
          setFinishLoading()
        }
      }
    }
  }

  fun setTailorId(tailorId: String) {
    this.tailorId = tailorId
  }
}