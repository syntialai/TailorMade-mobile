package com.mta.blibli.tailormade_auth.feature.login.viewmodel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.mta.blibli.tailormade_auth.core.repository.AuthRepository

class LoginViewModel @ViewModelInject constructor(
    private val authRepository: AuthRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    override fun getLogName(): String =
        "com.mta.blibli.tailormade_auth.feature.login.viewmodel.LoginViewModel"
}