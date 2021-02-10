package com.future.tailormade_profile.core.repository

import com.future.tailormade.base.test.BaseTest
import com.future.tailormade_profile.core.repository.impl.ProfileRepositoryImpl
import com.future.tailormade_profile.core.service.NominatimService
import com.future.tailormade_profile.core.service.ProfileService
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.stub
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
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
    dispatcher.runBlockingTest {

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
      }

      verifyNoMoreInteractions(profileService)
    }
  }
}