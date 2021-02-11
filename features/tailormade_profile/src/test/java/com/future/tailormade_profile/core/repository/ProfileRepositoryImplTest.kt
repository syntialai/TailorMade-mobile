package com.future.tailormade_profile.core.repository

import com.future.tailormade.base.test.BaseTest
import com.future.tailormade_profile.base.PayloadMapper
import com.future.tailormade_profile.core.repository.impl.ProfileRepositoryImpl
import com.future.tailormade_profile.core.service.NominatimService
import com.future.tailormade_profile.core.service.ProfileService
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.stub
import com.nhaarman.mockitokotlin2.verify
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
class ProfileRepositoryImplTest : BaseTest() {

  private lateinit var profileRepository: ProfileRepository

  @Mock
  private lateinit var profileService: ProfileService

  @Mock
  private lateinit var nominatimService: NominatimService

  private val dispatcher = TestCoroutineDispatcher()

  @Before
  override fun setUp() {
    MockitoAnnotations.openMocks(this)
    profileRepository = ProfileRepositoryImpl(profileService, nominatimService, dispatcher)
  }

  @After
  override fun tearDown() {
    Mockito.framework().clearInlineMocks()
  }

  @Test
  fun `Given when get profile info then success return mapped profile info response`() {
    val expectedResponse = PayloadMapper.getProfileInfoResponse()
    val expectedUiModel = PayloadMapper.getProfileInfoUiModel()

    dispatcher.runBlockingTest {
      profileService.stub {
        onBlocking { getProfileInfo(USER_ID) } doReturn generateSingleBaseResponse(
            data = expectedResponse)
      }

      val flow = profileRepository.getProfileInfo(USER_ID)

      flow.collect {
        verify(profileService).getProfileInfo(USER_ID)
        assertEquals(it.response, expectedResponse)
        assertEquals(it.uiModel, expectedUiModel)
      }

      verifyNoMoreInteractions(profileService)
    }
  }

  @Test
  fun `Given when get profile info and data returned is null then do nothing`() {
    dispatcher.runBlockingTest {
      profileService.stub {
        onBlocking { getProfileInfo(USER_ID) } doReturn generateSingleBaseResponse()
      }

      val flow = profileRepository.getProfileInfo(USER_ID)

      flow.collect {
        verify(profileService).getProfileInfo(USER_ID)
        verifyNoMoreInteractions(profileService)
      }
    }
  }

  @Test
  fun `Given when get tailor profile info then success return mapped profile info response`() {
    val expectedResponse = PayloadMapper.getTailorProfileInfoResponse()
    val expectedUiModel = PayloadMapper.getTailorProfileInfoUiModel()

    dispatcher.runBlockingTest {
      profileService.stub {
        onBlocking { getTailorProfileInfo(TAILOR_ID) } doReturn generateSingleBaseResponse(
            data = expectedResponse)
      }

      val flow = profileRepository.getTailorProfileInfo(TAILOR_ID)

      flow.collect {
        verify(profileService).getTailorProfileInfo(TAILOR_ID)
        assertEquals(it, expectedUiModel)
      }

      verifyNoMoreInteractions(profileService)
    }
  }

  @Test
  fun `Given when get tailor profile info and data returned is null then do nothing`() {
    dispatcher.runBlockingTest {
      profileService.stub {
        onBlocking { getTailorProfileInfo(TAILOR_ID) } doReturn generateSingleBaseResponse()
      }

      val flow = profileRepository.getTailorProfileInfo(TAILOR_ID)

      flow.collect {
        verify(profileService).getTailorProfileInfo(TAILOR_ID)
        verifyNoMoreInteractions(profileService)
      }
    }
  }

  @Test
  fun `Given when get profile designs then success return profile design response`() {
    val expectedResponse = generateListBaseResponse(
        data = PayloadMapper.getProfileDesignResponses())

    dispatcher.runBlockingTest {
      profileService.stub {
        onBlocking {
          getProfileTailorDesigns(TAILOR_ID, PAGE, ITEM_PER_PAGE)
        } doReturn expectedResponse
      }

      val flow = profileRepository.getProfileDesigns(TAILOR_ID, PAGE, ITEM_PER_PAGE)

      flow.collect {
        verify(profileService).getProfileTailorDesigns(TAILOR_ID, PAGE, ITEM_PER_PAGE)
        assertEquals(it, expectedResponse.data)
      }

      verifyNoMoreInteractions(profileService)
    }
  }

  @Test
  fun `Given when get profile designs and data returned is null then do nothing`() {
    dispatcher.runBlockingTest {
      profileService.stub {
        onBlocking {
          getProfileTailorDesigns(TAILOR_ID, PAGE, ITEM_PER_PAGE)
        } doReturn generateListBaseResponse()
      }

      val flow = profileRepository.getProfileDesigns(TAILOR_ID, PAGE, ITEM_PER_PAGE)

      flow.collect {
        verify(profileService).getProfileTailorDesigns(TAILOR_ID, PAGE, ITEM_PER_PAGE)
        verifyNoMoreInteractions(profileService)
      }
    }
  }

  @Test
  fun `Given when search location then success return location responses`() {
    val query = "QUERY"
    val expectedResponse = PayloadMapper.getLocationResponses()

    dispatcher.runBlockingTest {
      nominatimService.stub {
        onBlocking {
          searchLocation(query, "json", "1", "5", "1")
        } doReturn expectedResponse
      }

      val flow = profileRepository.searchLocation(query)

      flow.collect {
        verify(nominatimService).searchLocation(query, "json", "1", "5", "1")
        assertEquals(it, expectedResponse)
      }

      verifyNoMoreInteractions(nominatimService)
    }
  }

  @Test
  fun `Given when update profile about then success return profile about response`() {
    val request = PayloadMapper.getUpdateProfileAboutRequest()
    val expectedResponse = generateSingleBaseResponse(
        data = PayloadMapper.getProfileAboutResponse())

    dispatcher.runBlockingTest {
      profileService.stub {
        onBlocking { updateProfileAboutInfo(USER_ID, request) } doReturn expectedResponse
      }

      val flow = profileRepository.updateProfileAbout(USER_ID, request)

      flow.collect {
        verify(profileService).updateProfileAboutInfo(USER_ID, request)
        assertEquals(it, expectedResponse.data)
      }

      verifyNoMoreInteractions(profileService)
    }
  }

  @Test
  fun `Given when update profile about and data returned is null then do nothing`() {
    val request = PayloadMapper.getUpdateProfileAboutRequest()

    dispatcher.runBlockingTest {
      profileService.stub {
        onBlocking {
          updateProfileAboutInfo(USER_ID, request)
        } doReturn generateSingleBaseResponse()
      }

      val flow = profileRepository.updateProfileAbout(USER_ID, request)

      flow.collect {
        verify(profileService).updateProfileAboutInfo(USER_ID, request)
        verifyNoMoreInteractions(profileService)
      }
    }
  }

  @Test
  fun `Given when update profile info then success return profile info response`() {
    val request = PayloadMapper.getUpdateProfileRequest()
    val expectedResponse = generateSingleBaseResponse(data = PayloadMapper.getProfileInfoResponse())

    dispatcher.runBlockingTest {
      profileService.stub {
        onBlocking { updateProfileInfo(USER_ID, request) } doReturn expectedResponse
      }

      val flow = profileRepository.updateProfileInfo(USER_ID, request)

      flow.collect {
        verify(profileService).updateProfileInfo(USER_ID, request)
        assertEquals(it, expectedResponse.data)
      }

      verifyNoMoreInteractions(profileService)
    }
  }

  @Test
  fun `Given when update profile info and data returned is null then do nothing`() {
    val request = PayloadMapper.getUpdateProfileRequest()

    dispatcher.runBlockingTest {
      profileService.stub {
        onBlocking { updateProfileInfo(USER_ID, request) } doReturn generateSingleBaseResponse()
      }

      val flow = profileRepository.updateProfileInfo(USER_ID, request)

      flow.collect {
        verify(profileService).updateProfileInfo(USER_ID, request)
        verifyNoMoreInteractions(profileService)
      }
    }
  }
}