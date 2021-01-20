package com.future.tailormade.core.repository

import com.future.tailormade.base.test.BaseTest
import com.future.tailormade.core.mapper.DashboardMapper
import com.future.tailormade.core.model.response.dashboard.DashboardTailorResponse
import com.future.tailormade.core.repository.impl.DashboardRepositoryImpl
import com.future.tailormade.core.service.DashboardService
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.stub
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class DashboardRepositoryImplTest: BaseTest() {

  companion object {
    private const val LAT = 2.33
    private const val LON = 92.99
    private const val PAGE = 1
    private const val ITEM_PER_PAGE = 10
  }

  @InjectMocks
  private lateinit var dashboardRepository: DashboardRepositoryImpl

  @Mock
  private lateinit var dashboardService: DashboardService

  @Before
  override fun setUp() {
    MockitoAnnotations.openMocks(this)
  }

  @After
  override fun tearDown() {
    Mockito.framework().clearInlineMocks()
  }

  @Test
  fun `Given when get dashboard tailors then success return mapped response`() {
    val expectedResponse = generateListBaseResponse(data = getDashboardTailorsResponse())
    val expectedUiModel = expectedResponse.data!!.map {
      DashboardMapper.mapToDashboardTailorUiModel(it)
    } as ArrayList

    runBlocking {
      dashboardService.stub {
        onBlocking { getDashboardTailors(LAT, LON, PAGE, ITEM_PER_PAGE) } doReturn expectedResponse
      }

      val flow = dashboardRepository.getDashboardTailors(LAT, LON, PAGE, ITEM_PER_PAGE)

      flow.collect {
        Mockito.verify(dashboardService).getDashboardTailors(LAT, LON, PAGE, ITEM_PER_PAGE)
        assertEquals(it, expectedUiModel)
      }
    }
  }

  private fun getDashboardTailorsResponse() = listOf(DashboardTailorResponse("TAILOR_1", "Tailor"))
}