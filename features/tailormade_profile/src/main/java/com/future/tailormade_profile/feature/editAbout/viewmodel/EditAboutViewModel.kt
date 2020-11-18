package com.future.tailormade_profile.feature.editAbout.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade_profile.core.model.entity.Education
import com.future.tailormade_profile.core.model.entity.Occupation
import com.future.tailormade_profile.core.model.response.ProfileAboutResponse

class EditAboutViewModel : BaseViewModel() {

  override fun getLogName(): String =
      "com.future.tailormade_profile.feature.editAbout.viewmodel.EditAboutViewModel"

  private var _profileAbout = MutableLiveData<ProfileAboutResponse>()
  val profileAbout: LiveData<ProfileAboutResponse>
    get() = _profileAbout

  fun updateProfileAbout(occupation: Occupation, education: Education) {
    // TODO: Implement API call
  }
}