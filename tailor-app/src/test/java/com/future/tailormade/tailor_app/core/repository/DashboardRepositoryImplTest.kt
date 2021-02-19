package com.future.tailormade.tailor_app.core.repository

import com.future.tailormade.base.test.BaseTest
import com.future.tailormade.tailor_app.base.PayloadMapper
import com.future.tailormade.tailor_app.core.repository.impl.DashboardRepositoryImpl
import com.future.tailormade.tailor_app.core.service.DashboardService
import com.future.tailormade_profile.core.service.ProfileService
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.stub
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
class DashboardRepositoryImplTest : BaseTest() {

  private lateinit var dashboardRepository: DashboardRepository

  @Mock
  private lateinit var profileService: ProfileService

  @Mock
  private lateinit var dashboardService: DashboardService

  private val dispatcher = TestCoroutineDispatcher()

  @Before
  override fun setUp() {
    MockitoAnnotations.openMocks(this)
    dashboardRepository = DashboardRepositoryImpl(profileService, dashboardService, dispatcher)
  }

  @After
  override fun tearDown() {
    Mockito.framework().clearInlineMocks()
  }

  @Test
  fun `Given when get dashboard designs then success return mapped response`() {
    val expectedResponse = generateListBaseResponse(
        data = PayloadMapper.getDashboardDesignsResponse())
    val expectedUiModel = PayloadMapper.getDashboardDesignsUiModel()

    dispatcher.runBlockingTest {
      profileService.stub {
        onBlocking {
          getProfileTailorDesigns(TAILOR_ID, PAGE, ITEM_PER_PAGE)
        } doReturn expectedResponse
      }

      val flow = dashboardRepository.getDashboardDesigns(TAILOR_ID, PAGE, ITEM_PER_PAGE)

      flow.collect {
        Mockito.verify(profileService).getProfileTailorDesigns(TAILOR_ID, PAGE, ITEM_PER_PAGE)
        assertEquals(it, expectedUiModel)
      }

      verifyNoMoreInteractions(profileService)
    }
  }

  @Test
  fun `Given when get dashboard designs and return null then do nothing`() {
    dispatcher.runBlockingTest {
      profileService.stub {
        onBlocking {
          getProfileTailorDesigns(TAILOR_ID, PAGE, ITEM_PER_PAGE)
        } doReturn generateListBaseResponse()
      }

      val flow = dashboardRepository.getDashboardDesigns(TAILOR_ID, PAGE, ITEM_PER_PAGE)

      flow.collect {
        Mockito.verify(profileService).getProfileTailorDesigns(TAILOR_ID, PAGE, ITEM_PER_PAGE)
        verifyNoMoreInteractions(profileService)
      }
    }
  }

  @Test
  fun `Given when delete dashboard design then success return response`() {
    dispatcher.runBlockingTest {
      dashboardService.stub {
        onBlocking { deleteDesignById(TAILOR_ID, DESIGN_ID) } doReturn generateBaseResponse(CODE_OK,
            STATUS_OK)
      }

      val flow = dashboardRepository.deleteDashboardDesign(TAILOR_ID, DESIGN_ID)

      flow.collect {
        Mockito.verify(dashboardService).deleteDesignById(TAILOR_ID, DESIGN_ID)
        verifyNoMoreInteractions(dashboardService)
      }
    }
  }
}