package com.future.tailormade_auth.feature.signIn.viewmodel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.config.Constants
import com.future.tailormade.util.extension.onError
import com.future.tailormade_auth.core.model.request.SignInRequest
import com.future.tailormade_auth.core.repository.AuthRepository
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect

class SignInViewModel @ViewModelInject constructor(
    private val authRepository: AuthRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    override fun getLogName(): String =
        "com.future.tailormade_auth.feature.signIn.viewmodel.SignInViewModel"

    private var _email: String = ""

    private var _password: String = ""

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?>
        get() = _errorMessage

    fun setEmail(email: String) {
        _email = email
    }

    fun setPassword(password: String) {
        _password = password
    }

    @InternalCoroutinesApi
    fun signIn() {
        val signInRequest = SignInRequest(_email, _password)

        launchViewModelScope {
            authRepository.signIn(signInRequest)
                .onError { error ->
                    appLogger.logOnError(error.message.orEmpty(), error)
                    _errorMessage.value = Constants.SIGN_IN_ERROR
                }.collect {
                    _errorMessage.value = null
                }
        }
    }
}