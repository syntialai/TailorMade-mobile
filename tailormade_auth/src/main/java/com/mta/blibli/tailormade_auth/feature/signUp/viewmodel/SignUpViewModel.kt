package com.mta.blibli.tailormade_auth.feature.signUp.viewmodel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.config.Constants
import com.future.tailormade.util.extension.onError
import com.mta.blibli.tailormade_auth.core.model.request.SignUpRequest
import com.mta.blibli.tailormade_auth.core.repository.AuthRepository
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect

class SignUpViewModel @ViewModelInject constructor(
    private val authRepository: AuthRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    override fun getLogName(): String =
        "com.mta.blibli.tailormade_auth.feature.signUp.viewmodel.SignUpViewModel"

    private var _phoneNumber: String = ""

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?>
        get() = _errorMessage

    fun setPhoneNumber(phoneNumber: String) {
        _phoneNumber = phoneNumber
    }

    @InternalCoroutinesApi
    fun signUp(name: String, email: String, birthDate: String) {
        val signUpRequest = SignUpRequest(name, email, _phoneNumber, birthDate)

        launchViewModelScope {
            authRepository.signUp(signUpRequest)
                .onError { error ->
                    appLogger.logOnError(error.message.orEmpty(), error)
                    _errorMessage.value = Constants.SIGN_UP_ERROR
                }.collect {
                    _errorMessage.value = null
                }
        }
    }
}