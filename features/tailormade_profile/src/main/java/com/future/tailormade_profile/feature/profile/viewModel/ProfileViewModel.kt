package com.future.tailormade_profile.feature.profile.viewModel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.config.Constants
import com.future.tailormade.util.extension.onError
import com.future.tailormade_auth.core.repository.impl.AuthSharedPrefRepository
import com.future.tailormade_profile.core.model.entity.Location
import com.future.tailormade_profile.core.model.response.ProfileInfoResponse
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
          setErrorMessage(Constants.FAILED_TO_GET_PROFILE_INFO)
        }.collectLatest { response ->
          response.data?.let {
            _profileInfoUiModel.value = mapToProfileInfoUiModel(it)
          }
        }
      }
    }
  }

  fun isUserProfile(id: String) = authSharedPrefRepository.userId == id

  private fun getAddress(location: Location?) = location?.city.orEmpty() + if (
      location?.province.isNullOrBlank().not()) {
        ", ${location?.province}"
      } else {
        ""
      }

  private fun mapToProfileInfoUiModel(profileInfo: ProfileInfoResponse) = ProfileInfoUiModel(
      id = profileInfo.id,
      name = profileInfo.name,
      image = profileInfo.image,
      phoneNumber = profileInfo.phoneNumber,
      birthDate = profileInfo.birthDate,
      address = getAddress(profileInfo.location),
      location = profileInfo.location,
      occupation = profileInfo.occupation,
      education = profileInfo.education
  )
}