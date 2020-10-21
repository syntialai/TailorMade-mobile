package com.mta.blibli.tailormade_auth.feature.login.viewmodel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.config.Constants
import com.future.tailormade.util.extension.onError
import com.mta.blibli.tailormade_auth.core.model.request.VerifyPhoneRequest
import com.mta.blibli.tailormade_auth.core.repository.AuthRepository
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect

class LoginViewModel @ViewModelInject constructor(
    private val authRepository: AuthRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    override fun getLogName(): String =
        "com.mta.blibli.tailormade_auth.feature.login.viewmodel.LoginViewModel"

    private var _phoneNumber: String? = null

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?>
        get() = _errorMessage

    fun getPhoneNumber(): String? = _phoneNumber

    fun setPhoneNumber(phoneNumber: String) {
        _phoneNumber = phoneNumber
    }

    @InternalCoroutinesApi
    fun submitPhoneNumber() {
        _phoneNumber?.let {
            val verifyPhoneRequest = VerifyPhoneRequest()

            launchViewModelScope {
                authRepository.verifyPhone(verifyPhoneRequest)
                    .onError { error ->
                        appLogger.logOnError(error.message.orEmpty(), error)
                        _errorMessage.value = Constants.PHONE_NUMBER_IS_NOT_VALID
                    }.collect {
                        _errorMessage.value = null
                    }
            }
        }
    }
}