package com.future.tailormade_auth.feature.signUp.viewmodel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.future.tailormade.base.model.enums.GenderEnum
import com.future.tailormade.base.model.enums.RoleEnum
import com.future.tailormade.base.repository.AuthSharedPrefRepository
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.config.Constants
import com.future.tailormade.util.extension.onError
import com.future.tailormade_auth.core.model.request.SignInRequest
import com.future.tailormade_auth.core.model.request.SignUpRequest
import com.future.tailormade_auth.core.model.response.TokenDetailResponse
import com.future.tailormade_auth.core.model.response.UserResponse
import com.future.tailormade_auth.core.repository.AuthRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart

class SignUpViewModel @ViewModelInject constructor(
  private val authRepository: AuthRepository,
  private val authSharedPrefRepository: AuthSharedPrefRepository,
  @Assisted private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

  companion object {
    private const val SIGN_UP_REQUEST = "SIGN_UP_REQUEST"
    private const val HAS_SIGN_IN = "HAS_SIGN_IN"
  }

  override fun getLogName(): String =
    "com.mta.blibli.tailormade_auth.feature.signUp.viewmodel.SignUpViewModel"

  private var signUpRequest: SignUpRequest? = null
  private var birthDate: Long? = null

  private var _hasSignIn: MutableLiveData<Boolean>
  val hasSignIn: LiveData<Boolean>
    get() = _hasSignIn

  init {
    signUpRequest = savedStateHandle.get(SIGN_UP_REQUEST)
    _hasSignIn = savedStateHandle.getLiveData(HAS_SIGN_IN, false)
  }

  private fun getSignInInfo() = SignInRequest(
    signUpRequest?.email.orEmpty(),
    signUpRequest?.password.orEmpty(),
    signUpRequest?.role ?: getRole()
  )

  private fun getGenderEnum(gender: String) = when(gender) {
    "Other" -> GenderEnum.Anonymous
    else -> GenderEnum.valueOf(gender)
  }

  fun setSignUpInfo(name: String, email: String, password: String) {
    birthDate?.let {
      signUpRequest = SignUpRequest(name, email, password, it)
    }
  }

  fun setSignUpBirthDate(timestamp: Long) {
    birthDate = timestamp
  }

  fun setSignUpGender(gender: String) {
    signUpRequest = signUpRequest?.copy(gender = getGenderEnum(gender), role = getRole())
  }

  private fun saveUserData(user: UserResponse) {
    with(authSharedPrefRepository) {
      userId = user.id
      name = user.name
      username = user.email
      userRole = RoleEnum.valueOf(user.role).ordinal
      userGender = GenderEnum.valueOf(user.gender).ordinal
    }
  }

  @ExperimentalCoroutinesApi
  @InternalCoroutinesApi
  fun signUp() {
    launchViewModelScope {
      signUpRequest?.let { request ->
        authRepository.signUp(request).onStart {
          setStartLoading()
        }.onError {
          appLogger.logOnError(Constants.SIGN_UP_ERROR, it)
          setFinishLoading()
          setErrorMessage(Constants.SIGN_UP_ERROR)
        }.flatMapLatest { response ->
          saveUserData(response)
          authRepository.signIn(getSignInInfo())
        }.onError {
          setErrorMessage(Constants.SIGN_IN_ERROR)
          setFinishLoading()
          _hasSignIn.value = false
        }.collectLatest { data ->
          updateToken(data.token)
          setFinishLoading()
          _hasSignIn.value = true
        }
      }
    }
  }

  private fun getRole() = RoleEnum.values()[authSharedPrefRepository.userRole]

  private fun updateToken(token: TokenDetailResponse) {
    with(authSharedPrefRepository) {
      accessToken = token.access
      refreshToken = token.refresh
    }
  }
}