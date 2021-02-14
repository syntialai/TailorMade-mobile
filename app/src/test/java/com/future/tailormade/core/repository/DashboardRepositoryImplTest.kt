package com.future.tailormade.core.repository

import com.future.tailormade.base.PayloadMapper
import com.future.tailormade.base.test.BaseTest
import com.future.tailormade.core.repository.impl.DashboardRepositoryImpl
import com.future.tailormade.core.service.DashboardService
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
  private lateinit var dashboardService: DashboardService

  private val dispatcher = TestCoroutineDispatcher()

  @Before
  override fun setUp() {
    MockitoAnnotations.openMocks(this)
    dashboardRepository = DashboardRepositoryImpl(dashboardService, dispatcher)
  }

  @After
  override fun tearDown() {
    Mockito.framework().clearInlineMocks()
  }

  @Test
  fun `Given when get dashboard tailors then success return mapped response`() {
    val expectedResponse = generateListBaseResponse(data = PayloadMapper.getDashboardTailorsResponse())
    val expectedUiModel = PayloadMapper.getDashboardTailorsUiModel()

    dispatcher.runBlockingTest {
      dashboardService.stub {
        onBlocking { getDashboardTailors(PAGE, ITEM_PER_PAGE) } doReturn expectedResponse
      }

      val flow = dashboardRepository.getDashboardTailors(PAGE, ITEM_PER_PAGE)

      flow.collect {
        Mockito.verify(dashboardService).getDashboardTailors(PAGE, ITEM_PER_PAGE)
        assertEquals(it, expectedUiModel)
      }

      verifyNoMoreInteractions(dashboardService)
    }
  }

  @Test
  fun `Given when get dashboard tailors and return null then do nothing`() {
    dispatcher.runBlockingTest {
      dashboardService.stub {
        onBlocking { getDashboardTailors(PAGE, ITEM_PER_PAGE) } doReturn generateListBaseResponse()
      }

      val flow = dashboardRepository.getDashboardTailors(PAGE, ITEM_PER_PAGE)

      flow.collect {
        Mockito.verify(dashboardService).getDashboardTailors(PAGE, ITEM_PER_PAGE)
        verifyNoMoreInteractions(dashboardService)
      }
    }
  }
}