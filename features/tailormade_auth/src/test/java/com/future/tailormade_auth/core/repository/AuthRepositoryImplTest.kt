package com.future.tailormade_auth.core.repository

import com.future.tailormade.base.test.BaseTest
import com.future.tailormade_auth.core.model.request.RefreshTokenRequest
import com.future.tailormade_auth.core.model.request.SignInRequest
import com.future.tailormade_auth.core.model.request.SignUpRequest
import com.future.tailormade_auth.core.model.response.SignInResponse
import com.future.tailormade_auth.core.model.response.TokenDetailResponse
import com.future.tailormade_auth.core.model.response.TokenResponse
import com.future.tailormade_auth.core.model.response.UserResponse
import com.future.tailormade_auth.core.repository.impl.AuthRepositoryImpl
import com.future.tailormade_auth.core.service.AuthLoginService
import com.future.tailormade_auth.core.service.AuthService
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.stub
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

  companion object {
    private const val ACCESS_TOKEN = "ACCESS TOKEN"
    private const val REFRESH_TOKEN = "REFRESH TOKEN"
  }

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
    val expectedResponse = generateSingleBaseResponse(data = TokenResponse(getTokenDetailResponse()))

    dispatcher.runBlockingTest {
      authService.stub {
        onBlocking { refreshToken(request) } doReturn expectedResponse
      }

      val flow = authRepository.refreshToken(request)

      flow.collect {
        Mockito.verify(authService).refreshToken(request)
        assertEquals(expectedResponse.data?.token, it)
      }
    }
  }

  @Test
  fun `Given when sign in then success return sign in response`() {
    val request = getSignInRequest()
    val expectedResponse = generateSingleBaseResponse(data = getSignInResponse())

    dispatcher.runBlockingTest {
      authLoginService.stub {
        onBlocking { signIn(request) } doReturn expectedResponse
      }

      val flow = authRepository.signIn(request)

      flow.collect {
        Mockito.verify(authLoginService).signIn(request)
        assertEquals(expectedResponse.data, it)
      }
    }
  }

  @Test
  fun `Given when sign up then success return user response`() {
    val request = getSignUpRequest()
    val expectedResponse = generateSingleBaseResponse(data = getUserResponse())

    dispatcher.runBlockingTest {
      authLoginService.stub {
        onBlocking { signUp(request) } doReturn expectedResponse
      }

      val flow = authRepository.signUp(request)

      flow.collect {
        Mockito.verify(authLoginService).signUp(request)
        assertEquals(expectedResponse.data, it)
      }
    }
  }

  private fun getRefreshTokenRequest() = RefreshTokenRequest(refreshToken = REFRESH_TOKEN)

  private fun getSignInRequest() = SignInRequest(username = USER_EMAIL, password = USER_PASSWORD,
      role = USER_ROLE)

  private fun getSignUpRequest() = SignUpRequest(name = USER_NAME, email = USER_EMAIL,
      password = USER_PASSWORD, birthDate = USER_BIRTHDATE, gender = USER_GENDER, role = USER_ROLE)

  private fun getSignInResponse() = SignInResponse(token = getTokenDetailResponse(),
      user = getUserResponse())

  private fun getUserResponse() = UserResponse(id = USER_ID, name = USER_NAME, email = USER_EMAIL,
      gender = USER_GENDER.name, role = USER_ROLE.name)

  private fun getTokenDetailResponse() = TokenDetailResponse(access = ACCESS_TOKEN,
      refresh = REFRESH_TOKEN)
}