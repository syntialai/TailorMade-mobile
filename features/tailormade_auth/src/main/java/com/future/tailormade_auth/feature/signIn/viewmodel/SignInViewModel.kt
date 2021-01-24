package com.future.tailormade_auth.feature.signIn.viewmodel

import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.future.tailormade.base.repository.AuthSharedPrefRepository
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.config.Constants
import com.future.tailormade.util.extension.onError
import com.future.tailormade_auth.core.model.request.SignInRequest
import com.future.tailormade_auth.core.model.response.TokenDetailResponse
import com.future.tailormade_auth.core.model.response.UserResponse
import com.future.tailormade_auth.core.repository.AuthRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest

class SignInViewModel @ViewModelInject constructor(
  private val authRepository: AuthRepository,
  private val authSharedPrefRepository: AuthSharedPrefRepository,
  @Assisted private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

  companion object {
    private const val USER_INFO = "USER_INFO"
  }

  override fun getLogName(): String =
      "com.future.tailormade_auth.feature.signIn.viewmodel.SignInViewModel"

  private var _userInfo: MutableLiveData<UserResponse>
  val userInfo: LiveData<UserResponse>
    get() = _userInfo

  init {
    _userInfo = savedStateHandle.getLiveData(USER_INFO)
  }

  @ExperimentalCoroutinesApi
  @InternalCoroutinesApi
  fun signIn(email: String, password: String) {
    val signInRequest = SignInRequest(email, password)

    launchViewModelScope {
      authRepository.signIn(signInRequest).onError {
        appLogger.logOnError(Constants.SIGN_IN_ERROR, it)
        setErrorMessage(Constants.SIGN_IN_ERROR)
      }.collectLatest { data ->
        updateToken(data.token)
        updateUserData(data.user)
      }
    }
  }

  private fun updateUserData(user: UserResponse) {
    with(authSharedPrefRepository) {
      userId = user.id
      username = user.email
      name = user.name
      userRole = user.role.ordinal
      userGender = user.gender.ordinal
    }
    _userInfo.value = user
  }

  private fun updateToken(token: TokenDetailResponse) {
    with(authSharedPrefRepository) {
      accessToken = token.access
      refreshToken = token.refresh
    }
  }
}