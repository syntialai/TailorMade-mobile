package com.mta.blibli.tailormade_auth.feature.verifyPhone.viewmodel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.config.Constants
import com.future.tailormade.util.extension.onError
import com.mta.blibli.tailormade_auth.core.model.request.LoginRequest
import com.mta.blibli.tailormade_auth.core.repository.AuthRepository
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect

class VerifyPhoneViewModel @ViewModelInject constructor(
    private val authRepository: AuthRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    override fun getLogName(): String =
        "com.mta.blibli.tailormade_auth.feature.verifyPhone.viewmodel.VerifyPhoneViewModel"

    private var _phoneNumber: String = ""

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?>
        get() = _errorMessage

    fun setPhoneNumber(phoneNumber: String) {
        _phoneNumber = phoneNumber
    }

    @InternalCoroutinesApi
    fun submitVerificationCode(verificationCode: String) {
        val loginRequest = LoginRequest(_phoneNumber, verificationCode)

        launchViewModelScope {
            authRepository.login(loginRequest)
                .onError { error ->
                    appLogger.logOnError(error.message.orEmpty(), error)
                    _errorMessage.value = Constants.VERIFICATION_CODE_IS_WRONG
                }.collect {
                    _errorMessage.value = null
                }
        }
    }
}