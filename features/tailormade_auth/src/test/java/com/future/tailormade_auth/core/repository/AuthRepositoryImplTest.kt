package com.future.tailormade_auth.core.repository

import com.future.tailormade.base.test.BaseTest
import com.future.tailormade_auth.base.PayloadMapper
import com.future.tailormade_auth.core.model.request.RefreshTokenRequest
import com.future.tailormade_auth.core.model.response.TokenResponse
import com.future.tailormade_auth.core.repository.impl.AuthRepositoryImpl
import com.future.tailormade_auth.core.service.AuthLoginService
import com.future.tailormade_auth.core.service.AuthService
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.stub
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class AuthRepositoryImplTest : BaseTest() {

  private lateinit var authRepository: AuthRepository

  @Mock
  private lateinit var authService: AuthService

  @Mock
  private lateinit var authLoginService: AuthLoginService

  private val dispatcher = TestCoroutineDispatcher()

  @Before
  override fun setUp() {
    MockitoAnnotations.openMocks(this)
    authRepository = AuthRepositoryImpl(authService, authLoginService, dispatcher)
  }

  @After
  override fun tearDown() {
    Mockito.framework().clearInlineMocks()
  }

  @Test
  fun `Given when refresh token then success return token detail response`() {
    val request = getRefreshTokenRequest()
    val expectedResponse = generateSingleBaseResponse(
        data = TokenResponse(PayloadMapper.getTokenDetailResponse()))

    dispatcher.runBlockingTest {
      authService.stub {
        onBlocking { refreshToken(request) } doReturn expectedResponse
      }

      val flow = authRepository.refreshToken(request)

      flow.collect {
        Mockito.verify(authService).refreshToken(request)
        assertEquals(expectedResponse.data?.token, it)
      }

      verifyNoMoreInteractions(authService)
    }
  }

  @Test
  fun `Given when refresh token and data returned is null then do nothing`() {
    val request = getRefreshTokenRequest()

    dispatcher.runBlockingTest {
      authService.stub {
        onBlocking { refreshToken(request) } doReturn generateSingleBaseResponse()
      }

      val flow = authRepository.refreshToken(request)

      flow.collect {
        Mockito.verify(authService).refreshToken(request)
      }
    }
  }

  @Test
  fun `Given when sign in then success return sign in response`() {
    val request = PayloadMapper.getSignInRequest()
    val expectedResponse = generateSingleBaseResponse(data = PayloadMapper.getSignInResponse())

    dispatcher.runBlockingTest {
      authLoginService.stub {
        onBlocking { signIn(request) } doReturn expectedResponse
      }

      val flow = authRepository.signIn(request)

      flow.collect {
        Mockito.verify(authLoginService).signIn(request)
        assertEquals(expectedResponse.data, it)
      }

      verifyNoMoreInteractions(authLoginService)
    }
  }

  @Test
  fun `Given when sign in and data returned is null then do nothing`() {
    val request = PayloadMapper.getSignInRequest()

    dispatcher.runBlockingTest {
      authLoginService.stub {
        onBlocking { signIn(request) } doReturn generateSingleBaseResponse()
      }

      val flow = authRepository.signIn(request)

      flow.collect {
        Mockito.verify(authLoginService).signIn(request)
      }
    }
  }

  @Test
  fun `Given when sign up then success return user response`() {
    val request = PayloadMapper.getSignUpRequest()
    val expectedResponse = generateSingleBaseResponse(data = PayloadMapper.getUserResponse())

    dispatcher.runBlockingTest {
      authLoginService.stub {
        onBlocking { signUp(request) } doReturn expectedResponse
      }

      val flow = authRepository.signUp(request)

      flow.collect {
        Mockito.verify(authLoginService).signUp(request)
        assertEquals(expectedResponse.data, it)
      }

      verifyNoMoreInteractions(authLoginService)
    }
  }

  @Test
  fun `Given when sign up and data returned is null then do nothing`() {
    val request = PayloadMapper.getSignUpRequest()

    dispatcher.runBlockingTest {
      authLoginService.stub {
        onBlocking { signUp(request) } doReturn generateSingleBaseResponse()
      }

      val flow = authRepository.signUp(request)

      flow.collect {
        Mockito.verify(authLoginService).signUp(request)
      }
    }
  }

  private fun getRefreshTokenRequest() = RefreshTokenRequest(
      refreshToken = PayloadMapper.REFRESH_TOKEN)
}