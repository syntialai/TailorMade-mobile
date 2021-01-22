package com.future.tailormade_auth.feature.signUp.viewmodel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import com.future.tailormade.base.repository.AuthSharedPrefRepository
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.config.Constants
import com.future.tailormade.util.extension.flowOnIOwithLoadingDialog
import com.future.tailormade.util.extension.onError
import com.future.tailormade_auth.core.model.request.SignInRequest
import com.future.tailormade_auth.core.model.request.SignUpRequest
import com.future.tailormade_auth.core.model.response.UserResponse
import com.future.tailormade_auth.core.repository.AuthRepository
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

  private var signUpRequest: SignUpRequest? = null

  private fun getSignInInfo() = SignInRequest(
    signUpRequest?.email.orEmpty(),
    signUpRequest?.password.orEmpty()
  )

  fun setSignUpInfo(name: String, email: String, birthDate: String, password: String) {
    signUpRequest = SignUpRequest(name, email, password, birthDate)
  }

  fun setSignUpGender(gender: String) {
    signUpRequest = signUpRequest?.copy(gender = gender)
  }

  private fun saveUserData(user: UserResponse) {
    with(authSharedPrefRepository) {
      userId = user.id
      name = user.name
      username = user.email
      userRole = user.role
    }
  }

  @ExperimentalCoroutinesApi
  fun activateTailor() {
    launchViewModelScope {
      authRepository.activateTailor().onError { error ->
        appLogger.logOnError(error.message.orEmpty(), error)
      }.collect { response ->
        response.data?.let {
          authSharedPrefRepository.userRole = it.role ?: 0
        }
      }
    }
  }

  @ExperimentalCoroutinesApi
  @InternalCoroutinesApi
  fun signUp() {
    launchViewModelScope {
      signUpRequest?.let { request ->
        authRepository.signUp(request).flowOnIOwithLoadingDialog(this).onError { error ->
          appLogger.logOnError(error.message.orEmpty(), error)
          _errorMessage.value = Constants.SIGN_UP_ERROR
        }.flatMapLatest { response ->
          response.data?.let { saveUserData(it) }
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
}