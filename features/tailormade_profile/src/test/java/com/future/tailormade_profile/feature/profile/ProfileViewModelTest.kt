package com.future.tailormade_profile.feature.profile

import com.future.tailormade.base.model.BaseMapperModel
import com.future.tailormade.config.Constants
import com.future.tailormade_profile.base.BaseViewModelTest
import com.future.tailormade_profile.base.PayloadMapper
import com.future.tailormade_profile.feature.profile.viewModel.ProfileViewModel
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
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
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class ProfileViewModelTest : BaseViewModelTest() {

  private lateinit var viewModel: ProfileViewModel

  private val userIdCaptor = argumentCaptor<String>()

  @Before
  override fun setUp() {
    MockitoAnnotations.openMocks(this)
    viewModel = ProfileViewModel(profileRepository, authSharedPrefRepository, savedStateHandle)
  }

  @After
  override fun tearDown() {
    Mockito.framework().clearInlineMocks()
  }

  @Test
  fun `Given when fetch profile info then success update live data`() {
    val userAddress = "ADDRESS"
    val expectedResponse = PayloadMapper.getProfileInfoResponse()
    val expectedUiModel = PayloadMapper.getProfileInfoUiModel().apply {
      address = userAddress
    }

    rule.dispatcher.runBlockingTest {
      val flow = getFlow(BaseMapperModel(expectedResponse, expectedUiModel))
      whenever(authSharedPrefRepository.userId) doReturn USER_ID
      whenever(profileRepository.getProfileInfo(any())) doReturn flow

      viewModel.fetchProfileInfo()

      verify(profileRepository).getProfileInfo(userIdCaptor.capture())
      assertEquals(userIdCaptor.firstValue, USER_ID)
      delay(1000)

      viewModel.profileInfoUiModel.observeForTesting {
        assertEquals(viewModel.profileInfoUiModel.value, expectedUiModel)
      }
      assertIsNoAboutData()
      assertIsUser()
      verify(authSharedPrefRepository, times(2)).userId

      verifyNoMoreInteractions(authSharedPrefRepository, profileRepository)
    }
  }

  @Test
  fun `Given when fetch profile info and error then update error message`() {
    rule.dispatcher.runBlockingTest {
      whenever(authSharedPrefRepository.userId) doReturn USER_ID
      whenever(profileRepository.getProfileInfo(any())) doReturn getErrorFlow()

      viewModel.fetchProfileInfo()

      verify(authSharedPrefRepository).userId
      verify(profileRepository).getProfileInfo(userIdCaptor.capture())
      assertEquals(userIdCaptor.firstValue, USER_ID)
      delay(1000)

      viewModel.errorMessage.observeForTesting {
        assertEquals(viewModel.errorMessage.value, Constants.FAILED_TO_GET_PROFILE_INFO)
      }

      verifyNoMoreInteractions(authSharedPrefRepository, profileRepository)
    }
  }

  @Test
  fun `Given when fetch profile info and user id is null then do nothing`() {
    rule.dispatcher.runBlockingTest {
      whenever(authSharedPrefRepository.userId) doReturn null

      viewModel.fetchProfileInfo()

      verify(authSharedPrefRepository).userId
      verifyZeroInteractions(profileRepository)
      verifyNoMoreInteractions(authSharedPrefRepository)
    }
  }

  @Test
  fun `Given when get user gender then return user gender from shared preferences`() {
    whenever(authSharedPrefRepository.userGender) doReturn USER_GENDER_ORDINAL

    val response = viewModel.getUserGender()

    assertEquals(response, USER_GENDER_ORDINAL)
  }

  @Test
  fun `Given when check is no about data and data is empty then return true`() {
    val response = viewModel.isNoAboutData()
    assertTrue(response)
  }

  @Test
  fun `Given when check is user and data is empty then return false`() {
    whenever(authSharedPrefRepository.userId) doReturn USER_ID

    val response = viewModel.isUser()

    assertFalse(response)
  }

  private fun assertIsNoAboutData() {
    val response = viewModel.isNoAboutData()
    assertFalse(response)
  }

  private fun assertIsUser() {
    val response = viewModel.isUser()
    assertTrue(response)
  }
}