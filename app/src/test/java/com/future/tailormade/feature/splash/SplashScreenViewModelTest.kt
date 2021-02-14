package com.future.tailormade.feature.splash

import com.future.tailormade.base.BaseViewModelTest
import com.future.tailormade.base.model.enums.RoleEnum
import com.future.tailormade.feature.splash.viewModel.SplashScreenViewModel
import com.future.tailormade_auth.core.model.request.RefreshTokenRequest
import com.future.tailormade_auth.core.model.response.TokenDetailResponse
import com.future.tailormade_auth.core.repository.AuthRepository
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class SplashScreenViewModelTest : BaseViewModelTest() {

  companion object {
    private const val REFRESH_TOKEN = "REFRESH_TOKEN"
    private const val ACCESS_TOKEN = "ACCESS_TOKEN"
  }

  private lateinit var viewModel: SplashScreenViewModel

  private val authRepository = mock<AuthRepository>()

  private val tokenRequestCaptor = argumentCaptor<RefreshTokenRequest>()

  private val tokenResponseCaptor = argumentCaptor<String>()

  @Before
  override fun setUp() {
    MockitoAnnotations.openMocks(this)
    viewModel = SplashScreenViewModel(authRepository, authSharedPrefRepository)
    verify(authSharedPrefRepository).userRole = RoleEnum.ROLE_USER.ordinal
  }

  @After
  override fun tearDown() {
    Mockito.framework().clearInlineMocks()
  }

  @Test
  fun `Given when validate token then success update data`() {
    val expectedRequest = RefreshTokenRequest(REFRESH_TOKEN)
    val expectedResponse = TokenDetailResponse(REFRESH_TOKEN, ACCESS_TOKEN)

    rule.dispatcher.runBlockingTest {
      val flow = getFlow(expectedResponse)

      whenever(authSharedPrefRepository.refreshToken) doReturn REFRESH_TOKEN
      whenever(authRepository.refreshToken(any())) doReturn flow

      viewModel.validateToken()

      verify(authSharedPrefRepository).refreshToken
      verify(authRepository).refreshToken(tokenRequestCaptor.capture())
      assertEquals(tokenRequestCaptor.firstValue, expectedRequest)
      delay(1000)

      assertIsTokenExpired(false)
      verify(authSharedPrefRepository).setToken(tokenResponseCaptor.capture(),
          tokenResponseCaptor.capture())
      assertEquals(tokenResponseCaptor.firstValue, expectedResponse.access)
      assertEquals(tokenResponseCaptor.secondValue, expectedResponse.refresh)

      verifyNoMoreInteractions(authRepository, authSharedPrefRepository)
    }
  }

  @Test
  fun `Given when validate token and error then update data`() {
    val expectedRequest = RefreshTokenRequest(REFRESH_TOKEN)

    rule.dispatcher.runBlockingTest {
      whenever(authSharedPrefRepository.refreshToken) doReturn REFRESH_TOKEN
      whenever(authRepository.refreshToken(any())) doReturn getErrorFlow()

      viewModel.validateToken()

      verify(authSharedPrefRepository).refreshToken
      verify(authRepository).refreshToken(tokenRequestCaptor.capture())
      assertEquals(tokenRequestCaptor.firstValue, expectedRequest)
      delay(1000)

      assertIsTokenExpired(true)

      verifyNoMoreInteractions(authRepository, authSharedPrefRepository)
    }
  }

  @Test
  fun `Given when validate token and and refresh token is null then update data`() {
    rule.dispatcher.runBlockingTest {
      whenever(authSharedPrefRepository.refreshToken) doReturn null

      viewModel.validateToken()

      verify(authSharedPrefRepository).refreshToken
      assertIsTokenExpired(true)

      verifyZeroInteractions(authRepository)
      verifyNoMoreInteractions(authSharedPrefRepository)
    }
  }

  private fun assertIsTokenExpired(isTokenExpired: Boolean) {
    viewModel.isTokenExpired.observeForTesting {
      assertEquals(viewModel.isTokenExpired.value, isTokenExpired)
    }
  }
}