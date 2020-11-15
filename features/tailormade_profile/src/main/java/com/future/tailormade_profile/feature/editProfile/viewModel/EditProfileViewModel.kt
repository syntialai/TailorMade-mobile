package com.future.tailormade_profile.feature.editProfile.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.future.tailormade.base.viewmodel.BaseViewModel

class EditProfileViewModel : BaseViewModel() {

  override fun getLogName(): String =
      "com.future.tailormade_profile.feature.editProfile.viewModel.EditProfileViewModel"

  fun updateBasicInfo(name: String, phoneNumber: String, birthDate: String,
      location: String) {
    // TODO: Implement API call to update basic profile info
  }
}