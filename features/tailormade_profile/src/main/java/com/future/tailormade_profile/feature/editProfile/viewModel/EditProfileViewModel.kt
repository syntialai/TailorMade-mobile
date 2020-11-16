package com.future.tailormade_profile.feature.editProfile.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.util.coroutine.CoroutineHelper

class EditProfileViewModel : BaseViewModel() {

  override fun getLogName(): String =
      "com.future.tailormade_profile.feature.editProfile.viewModel.EditProfileViewModel"

  private var _listOfLocations = MutableLiveData<ArrayList<String>>()
  val listOfLocations: LiveData<ArrayList<String>>
    get() = _listOfLocations

  fun updateBasicInfo(name: String, phoneNumber: String, birthDate: String,
      location: String) {
    // TODO: Implement API call to update basic profile info
  }

  fun updateLocations(query: String) {
    // TODO: Implement API call to nomimatim API
  }
}