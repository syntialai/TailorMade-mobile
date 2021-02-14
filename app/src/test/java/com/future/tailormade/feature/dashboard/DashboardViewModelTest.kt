package com.future.tailormade.feature.dashboard

import com.future.tailormade.base.BaseViewModelTest
import com.future.tailormade.base.PayloadMapper
import com.future.tailormade.config.Constants
import com.future.tailormade.core.repository.DashboardRepository
import com.future.tailormade.feature.dashboard.viewModel.DashboardViewModel
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
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
class DashboardViewModelTest : BaseViewModelTest() {

  private lateinit var viewModel: DashboardViewModel

  private val dashboardRepository = mock<DashboardRepository>()

  @Before
  override fun setUp() {
    MockitoAnnotations.openMocks(this)
    viewModel = DashboardViewModel(dashboardRepository)
    whenever(authSharedPrefRepository.userRole) doReturn USER_ROLE_ORDINAL
  }

  @After
  override fun tearDown() {
    Mockito.framework().clearInlineMocks()
  }

  @Test
  fun `Given when fetch dashboard tailors then success update live data`() {
    val expectedUiModel = PayloadMapper.getDashboardTailorsUiModel()

    rule.dispatcher.runBlockingTest {
      val flow = getFlow(expectedUiModel)
      whenever(dashboardRepository.getDashboardTailors(PAGE, ITEM_PER_PAGE)) doReturn flow

      viewModel.fetchDashboardTailors()

      verify(dashboardRepository).getDashboardTailors(PAGE, ITEM_PER_PAGE)
      delay(1000)

      viewModel.tailors.observeForTesting {
        assertEquals(viewModel.tailors.value, expectedUiModel)
      }
      verifyNoMoreInteractions(dashboardRepository)
    }
  }

  @Test
  fun `Given when fetch dashboard tailors and error then update error message`() {
    rule.dispatcher.runBlockingTest {
      whenever(dashboardRepository.getDashboardTailors(PAGE, ITEM_PER_PAGE)) doReturn getErrorFlow()

      viewModel.refreshFetch()

      verify(dashboardRepository).getDashboardTailors(PAGE, ITEM_PER_PAGE)
      delay(1000)

      viewModel.errorMessage.observeForTesting {
        assertEquals(viewModel.errorMessage.value, Constants.generateFailedFetchError("dashboard"))
      }
      verifyNoMoreInteractions(dashboardRepository)
    }
  }

  @Test
  fun `Given when fetch more dashboard tailors then verify`() {
    rule.dispatcher.runBlockingTest {
      whenever(dashboardRepository.getDashboardTailors(PAGE + 1, ITEM_PER_PAGE)) doReturn getErrorFlow()

      viewModel.fetchMore()

      verify(dashboardRepository).getDashboardTailors(PAGE + 1, ITEM_PER_PAGE)
      delay(1000)

      viewModel.errorMessage.observeForTesting {
        assertEquals(viewModel.errorMessage.value, Constants.generateFailedFetchError("dashboard"))
      }
      verifyNoMoreInteractions(dashboardRepository)
    }
  }
}