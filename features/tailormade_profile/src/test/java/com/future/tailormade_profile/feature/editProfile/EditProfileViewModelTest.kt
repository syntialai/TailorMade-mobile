package com.future.tailormade_profile.feature.editProfile

import com.future.tailormade.base.model.BaseMapperModel
import com.future.tailormade.config.Constants
import com.future.tailormade_profile.base.BaseViewModelTest
import com.future.tailormade_profile.base.PayloadMapper
import com.future.tailormade_profile.core.model.request.UpdateProfileRequest
import com.future.tailormade_profile.core.model.response.ProfileInfoResponse
import com.future.tailormade_profile.feature.editProfile.viewModel.EditProfileViewModel
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.doReturn
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
class EditProfileViewModelTest : BaseViewModelTest() {

  private lateinit var viewModel: EditProfileViewModel

  private val userIdCaptor = argumentCaptor<String>()

  private val updateProfileInfoRequestCaptor = argumentCaptor<UpdateProfileRequest>()

  @Before
  override fun setUp() {
    MockitoAnnotations.openMocks(this)
    viewModel = EditProfileViewModel(profileRepository, authSharedPrefRepository, savedStateHandle)
  }

  @After
  override fun tearDown() {
    Mockito.framework().clearInlineMocks()
  }

  @Test
  fun `Given when get basic info then success update live data`() {
    val expectedResponse = PayloadMapper.getProfileInfoResponse()
    val expectedUiModel = PayloadMapper.getProfileInfoUiModel()

    rule.dispatcher.runBlockingTest {
      val flow = getFlow(BaseMapperModel(expectedResponse, expectedUiModel))
      whenever(authSharedPrefRepository.userId) doReturn USER_ID
      whenever(profileRepository.getProfileInfo(any())) doReturn flow

      viewModel.getBasicInfo()

      verify(authSharedPrefRepository).userId
      verify(profileRepository).getProfileInfo(userIdCaptor.capture())
      assertEquals(userIdCaptor.firstValue, USER_ID)
      assertLoading(true)
      delay(1000)

      assertProfileInfo(expectedResponse)
      verifyNoMoreInteractions(authSharedPrefRepository, profileRepository)
    }
  }

  @Test
  fun `Given when get basic info and error then update live data`() {
    rule.dispatcher.runBlockingTest {
      whenever(authSharedPrefRepository.userId) doReturn USER_ID
      whenever(profileRepository.getProfileInfo(any())) doReturn getErrorFlow()

      viewModel.getBasicInfo()

      verify(authSharedPrefRepository).userId
      verify(profileRepository).getProfileInfo(userIdCaptor.capture())
      assertEquals(userIdCaptor.firstValue, USER_ID)
      assertLoading(true)
      delay(1000)

      assertError(Constants.FAILED_TO_GET_PROFILE_INFO)
      verifyNoMoreInteractions(authSharedPrefRepository, profileRepository)
    }
  }

  @Test
  fun `Given when get basic info and user id is null then do nothing`() {
    rule.dispatcher.runBlockingTest {
      whenever(authSharedPrefRepository.userId) doReturn null

      viewModel.getBasicInfo()

      verify(authSharedPrefRepository).userId

      verifyZeroInteractions(profileRepository)
      verifyNoMoreInteractions(authSharedPrefRepository)
    }
  }

  @Test
  fun `Given when check is birth date valid when birth date is null then return false`() {
    val response = viewModel.isBirthDateValid()
    assertFalse(response)
  }

  @Test
  fun `Given when check is birth date valid when birth date has been set then return true`() {
    viewModel.setBirthDate(USER_BIRTHDATE)
    val response = viewModel.isBirthDateValid()
    assertTrue(response)
  }

  @Test
  fun `Given when update basic info then success update live data`() {
    val expectedRequest = PayloadMapper.getUpdateProfileRequest()
    val expectedResponse = PayloadMapper.getProfileInfoResponse()

    rule.dispatcher.runBlockingTest {
      val flow = getFlow(expectedResponse)
      whenever(authSharedPrefRepository.userId) doReturn USER_ID
      whenever(profileRepository.updateProfileInfo(any(), any())) doReturn flow

      viewModel.setBirthDate(expectedRequest.birthDate)
      viewModel.updateBasicInfo(expectedRequest.name, expectedRequest.phoneNumber)

      verify(authSharedPrefRepository).userId
      verify(profileRepository).updateProfileInfo(userIdCaptor.capture(),
          updateProfileInfoRequestCaptor.capture())
      assertEquals(userIdCaptor.firstValue, USER_ID)
      assertEquals(updateProfileInfoRequestCaptor.firstValue, expectedRequest)
      assertLoading(true)
      delay(1000)

      assertProfileInfo(expectedResponse)
      assertIsUpdated(true)

      verifyNoMoreInteractions(authSharedPrefRepository, profileRepository)
    }
  }

  @Test
  fun `Given when update basic info and error then update live data`() {
    val expectedRequest = PayloadMapper.getUpdateProfileRequest()
    val expectedResponse = PayloadMapper.getProfileInfoResponse()

    rule.dispatcher.runBlockingTest {
      whenever(authSharedPrefRepository.userId) doReturn USER_ID
      whenever(profileRepository.updateProfileInfo(any(), any())) doReturn getErrorFlow()

      viewModel.setBirthDate(expectedRequest.birthDate)
      viewModel.updateBasicInfo(expectedRequest.name, expectedRequest.phoneNumber)

      verify(authSharedPrefRepository).userId
      verify(profileRepository).updateProfileInfo(userIdCaptor.capture(),
          updateProfileInfoRequestCaptor.capture())
      assertEquals(userIdCaptor.firstValue, USER_ID)
      assertEquals(updateProfileInfoRequestCaptor.firstValue, expectedRequest)
      assertLoading(true)
      delay(1000)

      assertError(Constants.FAILED_TO_UPDATE_PROFILE)

      verifyNoMoreInteractions(authSharedPrefRepository, profileRepository)
    }
  }

  @Test
  fun `Given when update locations then success update live data`() {
    val query = "QUERY"
    val expectedResponse = PayloadMapper.getLocationResponses()

    rule.dispatcher.runBlockingTest {
      val flow = getFlow(expectedResponse)
      whenever(profileRepository.searchLocation(query)) doReturn flow

      viewModel.updateLocations(query)

      verify(profileRepository).searchLocation(query)
      delay(1000)

      viewModel.listOfLocations.observeForTesting {
        assertEquals(viewModel.listOfLocations.value, PayloadMapper.getMappedLocationResponses())
      }
      viewModel.setLocation(0)

      verifyNoMoreInteractions(profileRepository)
    }
  }

  @Test
  fun `Given when update locations and error then update error message`() {
    val query = "QUERY"
    val errorMessage = "Error message"

    rule.dispatcher.runBlockingTest {
      whenever(profileRepository.searchLocation(query)) doReturn getErrorFlow(errorMessage)

      viewModel.updateLocations(query)

      verify(profileRepository).searchLocation(query)
      delay(1000)

      assertError(errorMessage, false)

      verifyNoMoreInteractions(profileRepository)
    }
  }

  private fun assertError(error: String, checkLoading: Boolean = true) {
    if (checkLoading) {
      assertLoading(false)
    }
    viewModel.errorMessage.observeForTesting {
      assertEquals(viewModel.errorMessage.value, error)
    }
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

  private fun assertProfileInfo(response: ProfileInfoResponse) {
    assertLoading(false)
    viewModel.profileInfo.observeForTesting {
      assertEquals(viewModel.profileInfo.value, response)
    }
  }
}