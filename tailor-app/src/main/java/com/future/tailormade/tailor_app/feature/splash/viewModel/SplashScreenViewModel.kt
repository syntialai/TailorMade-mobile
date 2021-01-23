package com.future.tailormade.feature.splash.viewModel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.future.tailormade.base.repository.AuthSharedPrefRepository
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.util.extension.onError
import com.future.tailormade_auth.core.model.request.RefreshTokenRequest
import com.future.tailormade_auth.core.repository.AuthRepository
import kotlinx.coroutines.flow.collect

class SplashScreenViewModel @ViewModelInject constructor(private val authRepository: AuthRepository,
    private val authSharedPrefRepository: AuthSharedPrefRepository) : BaseViewModel() {

  override fun getLogName(): String = "com.future.tailormade.feature.splash.viewModel.SplashScreenViewModel"

  private var _isTokenExpired = MutableLiveData<Boolean>()
  val isTokenExpired: LiveData<Boolean>
    get() = _isTokenExpired

  fun validateToken() {
    launchViewModelScope {
      authSharedPrefRepository.refreshToken?.let { refreshToken ->
        val refreshTokenRequest = RefreshTokenRequest(refreshToken)
        authRepository.refreshToken(refreshTokenRequest).onError {
          _isTokenExpired.value = true
        }.collect { token ->
          _isTokenExpired.value = false
          authSharedPrefRepository.refreshToken = token.refresh
          authSharedPrefRepository.accessToken = token.access
        }
      }
    }
  }
}