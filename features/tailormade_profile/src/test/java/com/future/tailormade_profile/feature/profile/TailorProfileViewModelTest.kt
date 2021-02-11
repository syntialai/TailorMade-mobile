package com.future.tailormade_profile.feature.profile

import com.future.tailormade.config.Constants
import com.future.tailormade_profile.base.BaseViewModelTest
import com.future.tailormade_profile.base.PayloadMapper
import com.future.tailormade_profile.feature.profile.viewModel.TailorProfileViewModel
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.doReturn
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
class TailorProfileViewModelTest : BaseViewModelTest() {

  private lateinit var viewModel: TailorProfileViewModel

  private val tailorIdCaptor = argumentCaptor<String>()

  @Before
  override fun setUp() {
    MockitoAnnotations.openMocks(this)
    viewModel = TailorProfileViewModel(profileRepository, savedStateHandle)
  }

  @After
  override fun tearDown() {
    Mockito.framework().clearInlineMocks()
  }

  @Test
  fun `Given when fetch tailor profile info and tailor id has been set then success update live data`() {
    val expectedUiModel = PayloadMapper.getTailorProfileInfoUiModel()

    rule.dispatcher.runBlockingTest {
      val flow = getFlow(expectedUiModel)
      whenever(profileRepository.getTailorProfileInfo(any())) doReturn flow

      viewModel.setTailorId(TAILOR_ID)
      viewModel.tailorId.observeForTesting {
        assertEquals(viewModel.tailorId.value, TAILOR_ID)
      }

      viewModel.fetchTailorProfileInfo()

      verify(profileRepository).getTailorProfileInfo(tailorIdCaptor.capture())
      assertEquals(tailorIdCaptor.firstValue, TAILOR_ID)
      assertLoading(true)
      delay(1000)

      viewModel.profileInfoUiModel.observeForTesting {
        assertEquals(viewModel.profileInfoUiModel.value, expectedUiModel)
      }
      assertLoading(false)
      verifyNoMoreInteractions(profileRepository)
    }
  }

  @Test
  fun `Given when fetch tailor profile info and tailor id has been set and error then update error message`() {
    rule.dispatcher.runBlockingTest {
      whenever(profileRepository.getTailorProfileInfo(any())) doReturn getErrorFlow()

      viewModel.setTailorId(TAILOR_ID)
      viewModel.tailorId.observeForTesting {
        assertEquals(viewModel.tailorId.value, TAILOR_ID)
      }

      viewModel.fetchTailorProfileInfo()

      verify(profileRepository).getTailorProfileInfo(tailorIdCaptor.capture())
      assertEquals(tailorIdCaptor.firstValue, TAILOR_ID)
      assertLoading(true)
      delay(1000)

      assertLoading(false)
      viewModel.errorMessage.observeForTesting {
        assertEquals(viewModel.errorMessage.value, Constants.FAILED_TO_GET_PROFILE_INFO)
      }

      verifyNoMoreInteractions(profileRepository)
    }
  }

  @Test
  fun `Given when fetch tailor profile info and tailor id is null then do nothing`() {
    rule.dispatcher.runBlockingTest {
      viewModel.fetchTailorProfileInfo()
      verifyZeroInteractions(profileRepository)
    }
  }

  private fun assertLoading(isLoading: Boolean) {
    viewModel.isLoading.observeForTesting {
      assertEquals(viewModel.isLoading.value, isLoading)
    }
  }
}