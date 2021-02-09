package com.future.tailormade_auth.feature.signIn

import com.future.tailormade.util.extension.orFalse
import com.future.tailormade.util.extension.orTrue
import com.future.tailormade_auth.base.BaseViewModelTest
import com.future.tailormade_auth.base.PayloadMapper
import com.future.tailormade_auth.core.repository.impl.AuthRepositoryImpl
import com.future.tailormade_auth.feature.signIn.viewmodel.SignInViewModel
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
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
class SignInViewModelTest : BaseViewModelTest() {

  private val authRepository = mock<AuthRepositoryImpl>()

  private lateinit var viewModel: SignInViewModel

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

    runBlocking {
      val flow = getFlow(expectedResponse)
      whenever(authRepository.signIn(request)) doReturn flow
      whenever(authSharedPrefRepository.userRole) doReturn USER_ROLE_ORDINAL

      viewModel.signIn(USER_EMAIL, USER_PASSWORD)

      verify(authSharedPrefRepository).userRole
      delay(1000)

      assertTrue(viewModel.isLoading.value.orFalse())
      delay(1000)

      viewModel.isLoading.observeForTesting {
        assertFalse(viewModel.isLoading.value.orTrue())
      }
      viewModel.userInfo.observeForTesting {
        assertEquals(viewModel.userInfo.value, expectedResponse.user)
      }
    }
  }
}