package com.future.tailormade_auth.feature.signUp.viewmodel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.config.Constants
import com.future.tailormade.util.extension.onError
import com.future.tailormade_auth.core.model.request.SignInRequest
import com.future.tailormade_auth.core.model.request.SignUpRequest
import com.future.tailormade_auth.core.repository.AuthRepository
import com.future.tailormade_auth.core.repository.impl.AuthSharedPrefRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapLatest

class SignUpViewModel @ViewModelInject constructor(
  private val authRepository: AuthRepository,
  private val authSharedPrefRepository: AuthSharedPrefRepository,
  @Assisted private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

  override fun getLogName(): String =
    "com.mta.blibli.tailormade_auth.feature.signUp.viewmodel.SignUpViewModel"

  private var signUpRequest: SignUpRequest = SignUpRequest()

  private val _errorMessage = MutableLiveData<String?>()
  val errorMessage: LiveData<String?>
    get() = _errorMessage

  private fun getSignInInfo() = SignInRequest(
    signUpRequest.email,
    signUpRequest.password
  )

  fun setSignUpInfo(
    name: String, email: String, birthDate: String,
    password: String
  ) {
    signUpRequest.apply {
      this.name = name
      this.email = email
      this.birthDate = birthDate
      this.password = password
    }
  }

  fun setSignUpRole(role: String) {
    //        signUpRequest.role = role
  }

  fun setSignUpGender(gender: String) {
    signUpRequest.gender = gender
  }

  @ExperimentalCoroutinesApi
  @InternalCoroutinesApi
  fun signUp() {
    launchViewModelScope {
      authRepository.signUp(signUpRequest).onError { error ->
        appLogger.logOnError(error.message.orEmpty(), error)
        _errorMessage.value = Constants.SIGN_UP_ERROR
      }.flatMapLatest {
        authRepository.signIn(getSignInInfo())
      }.onError { error ->
        appLogger.logOnError(error.message.orEmpty(), error)
      }.collect { response ->
        response.data?.let {
          authSharedPrefRepository.accessToken = it.token?.access.orEmpty()
          authSharedPrefRepository.refreshToken = it.token?.refresh.orEmpty()
        }
      }
    }
  }
}