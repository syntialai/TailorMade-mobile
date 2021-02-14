package com.future.tailormade_auth.feature.signIn

import com.future.tailormade.base.model.enums.GenderEnum
import com.future.tailormade.base.model.enums.RoleEnum
import com.future.tailormade.config.Constants
import com.future.tailormade.util.extension.orFalse
import com.future.tailormade.util.extension.orTrue
import com.future.tailormade_auth.base.BaseViewModelTest
import com.future.tailormade_auth.base.PayloadMapper
import com.future.tailormade_auth.core.repository.impl.AuthRepositoryImpl
import com.future.tailormade_auth.feature.signIn.viewmodel.SignInViewModel
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.doNothing
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
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class SignInViewModelTest : BaseViewModelTest() {

  private val authRepository = mock<AuthRepositoryImpl>()

  private lateinit var viewModel: SignInViewModel

  private val tokenCaptor = argumentCaptor<String>()

  private val userDataCaptor = argumentCaptor<String>()

  private val userDataOrdinalCaptor = argumentCaptor<Int>()

  @Before
  override fun setUp() {
    viewModel = SignInViewModel(authRepository, authSharedPrefRepository, savedStateHandle)
  }

  @After
  override fun tearDown() {
    Mockito.framework().clearInlineMocks()
  }

  @Test
  fun `Given when sign in then success update token and user data`() {
    val request = PayloadMapper.getSignInRequest()
    val expectedResponse = PayloadMapper.getSignInResponse()

    rule.dispatcher.runBlockingTest {
      val flow = getFlow(expectedResponse)
      whenever(authRepository.signIn(request)) doReturn flow
      whenever(authSharedPrefRepository.userRole) doReturn USER_ROLE_ORDINAL

      viewModel.signIn(USER_EMAIL, USER_PASSWORD)

      verify(authSharedPrefRepository).userRole
      assertTrue(viewModel.isLoading.value.orFalse())
      delay(1000)

      verify(authRepository).signIn(request)
      viewModel.isLoading.observeForTesting {
        assertFalse(viewModel.isLoading.value.orTrue())
      }
      viewModel.userInfo.observeForTesting {
        assertEquals(viewModel.userInfo.value, expectedResponse.user)
      }

      verify(authSharedPrefRepository).setToken(tokenCaptor.capture(), tokenCaptor.capture())
      assertEquals(tokenCaptor.firstValue, expectedResponse.token.access)
      assertEquals(tokenCaptor.secondValue, expectedResponse.token.refresh)

      verify(authSharedPrefRepository).updateUser(userDataCaptor.capture(),
          userDataCaptor.capture(), userDataCaptor.capture(), userDataOrdinalCaptor.capture(),
          userDataOrdinalCaptor.capture())
      with(expectedResponse.user) {
        assertEquals(id, userDataCaptor.firstValue)
        assertEquals(name, userDataCaptor.secondValue)
        assertEquals(email, userDataCaptor.thirdValue)
        assertEquals(RoleEnum.valueOf(role).ordinal, userDataOrdinalCaptor.firstValue)
        assertEquals(GenderEnum.valueOf(gender).ordinal, userDataOrdinalCaptor.secondValue)
      }

      verifyNoMoreInteractions(authRepository, authSharedPrefRepository)
    }
  }

  @Test
  fun `Given when sign in and error then update error message`() {
    val request = PayloadMapper.getSignInRequest()

    rule.dispatcher.runBlockingTest {
      whenever(authRepository.signIn(request)) doReturn getErrorFlow()
      whenever(authSharedPrefRepository.userRole) doReturn USER_ROLE_ORDINAL

      viewModel.signIn(USER_EMAIL, USER_PASSWORD)

      verify(authSharedPrefRepository).userRole
      assertTrue(viewModel.isLoading.value.orFalse())
      delay(1000)

      verify(authRepository).signIn(request)
      viewModel.isLoading.observeForTesting {
        assertFalse(viewModel.isLoading.value.orTrue())
      }
      viewModel.errorMessage.observeForTesting {
        assertEquals(viewModel.errorMessage.value, Constants.SIGN_IN_ERROR)
      }

      verifyNoMoreInteractions(authRepository, authSharedPrefRepository)
    }
  }
}