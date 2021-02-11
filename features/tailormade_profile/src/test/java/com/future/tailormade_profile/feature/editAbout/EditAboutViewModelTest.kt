package com.future.tailormade_profile.feature.editAbout

import com.future.tailormade.config.Constants
import com.future.tailormade_profile.base.BaseViewModelTest
import com.future.tailormade_profile.base.PayloadMapper
import com.future.tailormade_profile.core.model.request.UpdateProfileAboutRequest
import com.future.tailormade_profile.feature.editAbout.viewmodel.EditAboutViewModel
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
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
class EditAboutViewModelTest : BaseViewModelTest() {

  private lateinit var viewModel: EditAboutViewModel

  private val userIdCaptor = argumentCaptor<String>()

  private val updateProfileAboutRequestCaptor = argumentCaptor<UpdateProfileAboutRequest>()

  @Before
  override fun setUp() {
    MockitoAnnotations.openMocks(this)
    viewModel = EditAboutViewModel(profileRepository, authSharedPrefRepository, savedStateHandle)
  }

  @After
  override fun tearDown() {
    Mockito.framework().clearInlineMocks()
  }

  @Test
  fun `Given when update profile about then success returned data and update live data`() {
    val expectedRequest = PayloadMapper.getUpdateProfileAboutRequest()
    val expectedResponse = PayloadMapper.getProfileAboutResponse()

    rule.dispatcher.runBlockingTest {
      val flow = getFlow(expectedResponse)
      whenever(authSharedPrefRepository.userId) doReturn USER_ID
      whenever(profileRepository.updateProfileAbout(any(), any())) doReturn flow

      viewModel.updateProfileAbout(occupation = expectedRequest.occupation!!,
          education = expectedRequest.education!!)

      verify(authSharedPrefRepository).userId
      verify(profileRepository).updateProfileAbout(userIdCaptor.capture(),
          updateProfileAboutRequestCaptor.capture())
      assertCaptors(expectedRequest)
      assertLoading(true)
      delay(1000)

      assertLoading(false)
      viewModel.profileAbout.observeForTesting {
        assertEquals(viewModel.profileAbout.value, expectedResponse)
      }
      assertIsUpdated(true)

      verifyNoMoreInteractions(profileRepository, authSharedPrefRepository)
    }
  }

  @Test
  fun `Given when update profile about and return error then update live data`() {
    val expectedRequest = PayloadMapper.getUpdateProfileAboutRequest()

    rule.dispatcher.runBlockingTest {
      whenever(authSharedPrefRepository.userId) doReturn USER_ID
      whenever(profileRepository.updateProfileAbout(any(), any())) doReturn getErrorFlow()

      viewModel.updateProfileAbout(occupation = expectedRequest.occupation!!,
          education = expectedRequest.education!!)

      verify(authSharedPrefRepository).userId
      verify(profileRepository).updateProfileAbout(userIdCaptor.capture(),
          updateProfileAboutRequestCaptor.capture())
      assertCaptors(expectedRequest)
      assertLoading(true)
      delay(1000)

      assertLoading(false)
      viewModel.errorMessage.observeForTesting {
        assertEquals(viewModel.errorMessage.value, Constants.FAILED_TO_UPDATE_PROFILE)
      }

      verifyNoMoreInteractions(profileRepository, authSharedPrefRepository)
    }
  }

  @Test
  fun `Given when update profile about and user id is null then do nothing`() {
    val expectedRequest = PayloadMapper.getUpdateProfileAboutRequest()

    rule.dispatcher.runBlockingTest {
      whenever(authSharedPrefRepository.userId) doReturn null

      viewModel.updateProfileAbout(occupation = expectedRequest.occupation!!,
          education = expectedRequest.education!!)

      verify(authSharedPrefRepository).userId
      verifyZeroInteractions(profileRepository)
      verifyNoMoreInteractions(authSharedPrefRepository)
    }
  }

  private fun assertCaptors(expectedRequest: UpdateProfileAboutRequest) {
    assertEquals(userIdCaptor.firstValue, USER_ID)
    assertEquals(updateProfileAboutRequestCaptor.firstValue, expectedRequest)
  }

  private fun assertIsUpdated(isUpdated: Boolean) {
    viewModel.isUpdated.observeForTesting {
      assertEquals(viewModel.isUpdated.value, isUpdated)
    }
  }

  private fun assertLoading(isLoading: Boolean) {
    viewModel.isLoading.observeForTesting {
      assertEquals(viewModel.isLoading.value, isLoading)
    }
  }
}