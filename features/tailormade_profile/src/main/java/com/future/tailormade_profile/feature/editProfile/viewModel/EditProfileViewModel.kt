package com.future.tailormade_profile.feature.editProfile.viewModel

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
import com.future.tailormade.util.extension.orZero
import com.future.tailormade_profile.core.model.entity.Location
import com.future.tailormade_profile.core.model.request.UpdateProfileRequest
import com.future.tailormade_profile.core.model.response.LocationResponse
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

  private var _isUpdated = MutableLiveData<Boolean>()
  val isUpdated: LiveData<Boolean>
    get() = _isUpdated

  private var _birthDate: Long? = null
  private var _location: Location? = null
  private var locations = arrayListOf<LocationResponse>()

  init {
    _profileInfo = savedStateHandle.getLiveData(PROFILE_INFO)
    getBasicInfo()
  }

  fun isBirthDateValid() = _birthDate.orZero() > 0

  fun setBirthDate(birthDate: Long) {
    _birthDate = birthDate
  }

  fun setLocation(position: Int) {
    val location = locations[position]
    _location = Location(lat = location.lat.toFloat(), lon = location.lon.toFloat(),
        address = location.display_name.orEmpty(),
        district = location.address?.city_district.orEmpty(),
        city = location.address?.city.orEmpty(), province = location.address?.state.orEmpty(),
        country = location.address?.country.orEmpty(),
        postCode = location.address?.postcode.orEmpty())
    Log.d("LOCATION", _location.toString())
  }

  @ExperimentalCoroutinesApi
  @InternalCoroutinesApi
  fun updateBasicInfo(name: String, phoneNumber: String?) {
    val request = UpdateProfileRequest(name = name, birthDate = _birthDate.orZero(),
        phoneNumber = phoneNumber.orEmpty(), location = _location)
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
          _isUpdated.value = true
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
        locations = it
        val response = it.map { item ->
          item.display_name.orEmpty()
        }
        _listOfLocations.value = response
      }
    }
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
          _birthDate = data.response.birthDate
        }
      }
    }
  }
}