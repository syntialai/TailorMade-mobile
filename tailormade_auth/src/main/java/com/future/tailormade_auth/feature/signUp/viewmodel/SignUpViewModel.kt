package com.future.tailormade_auth.feature.signUp.viewmodel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.config.Constants
import com.future.tailormade.util.extension.onError
import com.future.tailormade_auth.core.model.request.SignUpRequest
import com.future.tailormade_auth.core.repository.AuthRepository
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect

class SignUpViewModel @ViewModelInject constructor(
    private val authRepository: AuthRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    override fun getLogName(): String =
        "com.mta.blibli.tailormade_auth.feature.signUp.viewmodel.SignUpViewModel"

    private var _phoneNumber: String = ""

    private var signUpRequest: SignUpRequest = SignUpRequest()

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?>
        get() = _errorMessage

    fun setPhoneNumber(phoneNumber: String) {
        _phoneNumber = phoneNumber
    }

    fun setSignUpInfo(name: String, email: String, birthDate: String) {
        signUpRequest.apply {
            this.name = name
            this.email = email
            this.birthDate = birthDate
        }
    }

    fun setSignUpRole(role: String) {
//        signUpRequest.role = role
    }

    fun setSignUpGender(gender: String) {
        signUpRequest.gender = gender
    }

    @InternalCoroutinesApi
    fun signUp() {
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