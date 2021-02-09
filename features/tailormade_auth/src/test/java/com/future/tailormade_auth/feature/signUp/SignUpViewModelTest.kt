package com.future.tailormade_auth.feature.signUp

import com.future.tailormade.base.model.enums.GenderEnum
import com.future.tailormade.config.Constants
import com.future.tailormade.util.extension.orFalse
import com.future.tailormade.util.extension.orTrue
import com.future.tailormade_auth.base.BaseViewModelTest
import com.future.tailormade_auth.base.PayloadMapper
import com.future.tailormade_auth.core.model.request.SignInRequest
import com.future.tailormade_auth.core.repository.impl.AuthRepositoryImpl
import com.future.tailormade_auth.feature.signUp.viewmodel.SignUpViewModel
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class SignUpViewModelTest : BaseViewModelTest() {

  private lateinit var viewModel: SignUpViewModel

  private val authRepository = mock<AuthRepositoryImpl>()

  private val signInRequestCaptor = argumentCaptor<SignInRequest>()

  @Before
  override fun setUp() {
    viewModel = SignUpViewModel(authRepository, authSharedPrefRepository, savedStateHandle)
  }

  @After
  override fun tearDown() {
    Mockito.framework().clearInlineMocks()

  }

  @Test
  fun `Given when sign up then success call sign in and update token and user data`() {
    val request = PayloadMapper.getSignUpRequest()
    val signInRequest = PayloadMapper.getSignInRequest()
    val expectedResponse = PayloadMapper.getUserResponse()
    val expectedSignInResponse = PayloadMapper.getSignInResponse()

    runBlocking {
      viewModel.setSignUpBirthDate(USER_BIRTHDATE)
      viewModel.setSignUpInfo(USER_NAME, USER_EMAIL, USER_PASSWORD)
      viewModel.setSignUpGender(USER_GENDER.name)

      val signUpFlow = getFlow(expectedResponse)
      val signInFlow = getFlow(expectedSignInResponse)

      whenever(authRepository.signUp(request)) doReturn signUpFlow
      whenever(authRepository.signIn(signInRequest)) doReturn signInFlow

      viewModel.signUp()
      delay(1000)

      verify(authRepository).signUp(request)
      assertTrue(viewModel.isLoading.value.orFalse())
      delay(1000)

      verify(authRepository).signIn(signInRequestCaptor.capture())
      assertEquals(signInRequestCaptor.firstValue, signInRequest)

      delay(1000)
      viewModel.isLoading.observeForTesting {
        assertFalse(viewModel.isLoading.value.orTrue())
      }
      viewModel.hasSignIn.observeForTesting {
        assertTrue(viewModel.hasSignIn.value.orFalse())
      }

      verifyNoMoreInteractions(authRepository)
    }
  }

  @Test
  fun `Given when sign up then success call sign in and error sign in`() {
    val request = PayloadMapper.getSignUpRequest()
    val signInRequest = PayloadMapper.getSignInRequest()
    val expectedResponse = PayloadMapper.getUserResponse()

    runBlocking {
      viewModel.setSignUpBirthDate(USER_BIRTHDATE)
      viewModel.setSignUpInfo(USER_NAME, USER_EMAIL, USER_PASSWORD)
      viewModel.setSignUpGender(USER_GENDER.name)

      val signUpFlow = getFlow(expectedResponse)

      whenever(authRepository.signUp(request)) doReturn signUpFlow
      whenever(authRepository.signIn(signInRequest)) doAnswer {
        throw getError()
      }

      viewModel.signUp()
      delay(1000)

      verify(authRepository).signUp(request)
      assertTrue(viewModel.isLoading.value.orFalse())
      delay(1000)

      verify(authRepository).signIn(signInRequestCaptor.capture())
      assertEquals(signInRequestCaptor.firstValue, signInRequest)

      delay(1000)
      assertError(Constants.SIGN_IN_ERROR)
      viewModel.hasSignIn.observeForTesting {
        assertFalse(viewModel.hasSignIn.value.orTrue())
      }

      verifyNoMoreInteractions(authRepository)
    }
  }


  @Test
  fun `Given when sign up then error before sign in`() {
    val request = PayloadMapper.getSignUpRequest().copy(gender = GenderEnum.Anonymous)

    runBlocking {
      viewModel.setSignUpBirthDate(USER_BIRTHDATE)
      viewModel.setSignUpInfo(USER_NAME, USER_EMAIL, USER_PASSWORD)
      viewModel.setSignUpGender("Other")

      whenever(authRepository.signUp(request)) doReturn getErrorFlow()

      viewModel.signUp()

      verify(authRepository).signUp(request)
      delay(1000)
      assertTrue(viewModel.isLoading.value.orFalse())

      delay(1000)
      assertError(Constants.SIGN_UP_ERROR)
    }
  }

  @Test
  fun `Given when sign up and request is null then do nothing`() {
    viewModel.signUp()
  }

  private fun assertError(message: String) {
    viewModel.isLoading.observeForTesting {
      assertFalse(viewModel.isLoading.value.orTrue())
    }
    viewModel.errorMessage.observeForTesting {
      assertEquals(viewModel.errorMessage.value, message)
    }
  }
}