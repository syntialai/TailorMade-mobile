package com.future.tailormade_profile.feature.profile.viewModel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade_auth.core.repository.impl.AuthSharedPrefRepository
import com.future.tailormade_profile.core.repository.ProfileRepository

class ProfileViewModel @ViewModelInject constructor(
    private val profileRepository: ProfileRepository,
    private val authSharedPrefRepository: AuthSharedPrefRepository,
    @Assisted private val savedStateHandle: SavedStateHandle) :
    BaseViewModel() {

    override fun getLogName() = "com.future.tailormade_profile.feature.profile.viewModel.ProfileViewModel"

    // TODO: Implement the ViewModel
}