package com.future.tailormade_auth.feature.signIn.viewmodel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import com.future.tailormade.base.repository.AuthSharedPrefRepository
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.config.Constants
import com.future.tailormade.util.extension.flowOnIOwithLoadingDialog
import com.future.tailormade.util.extension.onError
import com.future.tailormade_auth.core.model.request.SignInRequest
import com.future.tailormade_auth.core.repository.AuthRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect

class SignInViewModel @ViewModelInject constructor(
  private val authRepository: AuthRepository,
  private val authSharedPrefRepository: AuthSharedPrefRepository,
  @Assisted private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

  override fun getLogName(): String = "SignInViewModel"

  @ExperimentalCoroutinesApi
  @InternalCoroutinesApi
  fun signIn(email: String, password: String) {
    val signInRequest = SignInRequest(email, password)

    launchViewModelScope {
      authRepository.signIn(signInRequest).flowOnIOwithLoadingDialog(this).onError { error ->
        appLogger.logOnError(error.message.orEmpty(), error)
        _errorMessage.value = Constants.SIGN_IN_ERROR
      }.collect { response ->
        _errorMessage.value = null
        response.data?.let {
          authSharedPrefRepository.accessToken = it.token?.access.orEmpty()
          authSharedPrefRepository.refreshToken = it.token?.refresh.orEmpty()
        }
      }
    }
  }
}