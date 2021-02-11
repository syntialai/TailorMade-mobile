package com.future.tailormade_profile.feature.profile

import com.future.tailormade.config.Constants
import com.future.tailormade_profile.base.BaseViewModelTest
import com.future.tailormade_profile.base.PayloadMapper
import com.future.tailormade_profile.core.model.response.ProfileDesignResponse
import com.future.tailormade_profile.feature.profile.viewModel.ProfileDesignViewModel
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
class ProfileDesignViewModelTest : BaseViewModelTest() {

  private lateinit var viewModel: ProfileDesignViewModel

  @Before
  override fun setUp() {
    MockitoAnnotations.openMocks(this)
    viewModel = ProfileDesignViewModel(profileRepository, savedStateHandle)
  }

  @After
  override fun tearDown() {
    Mockito.framework().clearInlineMocks()
  }

  @Test
  fun `Given when fetch images when id has been set then success update live data`() {
    val expectedResponse = arrayListOf<ProfileDesignResponse>()
    expectedResponse.addAll(PayloadMapper.getProfileDesignResponses())

    rule.dispatcher.runBlockingTest {
      val flow = getFlow(expectedResponse)
      whenever(profileRepository.getProfileDesigns(TAILOR_ID, PAGE, ITEM_PER_PAGE)) doReturn flow

      viewModel.setId(TAILOR_ID)
      viewModel.fetchImages()

      verify(profileRepository).getProfileDesigns(TAILOR_ID, PAGE, ITEM_PER_PAGE)
      delay(1000)

      viewModel.images.observeForTesting {
        assertEquals(viewModel.images.value, expectedResponse)
      }
      verifyNoMoreInteractions(profileRepository)
    }
  }

  @Test
  fun `Given when fetch images when id has been set and error then update error message`() {
    rule.dispatcher.runBlockingTest {
      whenever(profileRepository.getProfileDesigns(TAILOR_ID, PAGE + 1, ITEM_PER_PAGE)) doReturn getErrorFlow()

      viewModel.setId(TAILOR_ID)
      viewModel.fetchMore()

      verify(profileRepository).getProfileDesigns(TAILOR_ID, PAGE + 1, ITEM_PER_PAGE)
      delay(1000)

      viewModel.errorMessage.observeForTesting {
        assertEquals(viewModel.errorMessage.value,
            Constants.generateFailedFetchError("tailor designs"))
      }
      verifyNoMoreInteractions(profileRepository)
    }
  }

  @Test
  fun `Given when fetch images when id is null then do nothing`() {
    rule.dispatcher.runBlockingTest {
      viewModel.refreshFetch()
      verifyZeroInteractions(profileRepository)
    }
  }
}