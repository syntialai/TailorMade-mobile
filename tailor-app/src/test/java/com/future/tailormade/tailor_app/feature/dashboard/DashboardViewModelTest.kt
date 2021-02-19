package com.future.tailormade.tailor_app.feature.dashboard

import com.future.tailormade.base.model.response.BaseResponse
import com.future.tailormade.config.Constants
import com.future.tailormade.tailor_app.base.BaseViewModelTest
import com.future.tailormade.tailor_app.base.PayloadMapper
import com.future.tailormade.tailor_app.core.repository.DashboardRepository
import com.future.tailormade.tailor_app.feature.dashboard.viewModel.DashboardViewModel
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
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
class DashboardViewModelTest : BaseViewModelTest() {

  private lateinit var viewModel: DashboardViewModel

  private val dashboardRepository = mock<DashboardRepository>()

  private val idCaptor = argumentCaptor<String>()

  private val pageCaptor = argumentCaptor<Int>()

  @Before
  override fun setUp() {
    MockitoAnnotations.openMocks(this)
    viewModel = DashboardViewModel(dashboardRepository, authSharedPrefRepository, savedStateHandle)
  }

  @After
  override fun tearDown() {
    Mockito.framework().clearInlineMocks()
  }

  @Test
  fun `Given when fetch tailor designs then success update live data`() {
    val expectedUiModel = PayloadMapper.getDashboardDesignsUiModel()

    rule.dispatcher.runBlockingTest {
      val flow = getFlow(expectedUiModel)
      whenever(authSharedPrefRepository.userId) doReturn TAILOR_ID
      whenever(dashboardRepository.getDashboardDesigns(any(), any(), any())) doReturn flow

      viewModel.fetchTailorDesigns()

      verify(authSharedPrefRepository).userId
      verify(dashboardRepository).getDashboardDesigns(idCaptor.capture(), pageCaptor.capture(),
          pageCaptor.capture())
      assertEquals(idCaptor.firstValue, TAILOR_ID)
      assertEquals(pageCaptor.firstValue, PAGE)
      assertEquals(pageCaptor.secondValue, ITEM_PER_PAGE)
      delay(1000)

      viewModel.designs.observeForTesting {
        assertEquals(viewModel.designs.value, expectedUiModel)
      }

      verifyNoMoreInteractions(authSharedPrefRepository, dashboardRepository)
    }
  }

  @Test
  fun `Given when fetch tailor designs and error then update error message`() {
    rule.dispatcher.runBlockingTest {
      whenever(authSharedPrefRepository.userId) doReturn TAILOR_ID
      whenever(dashboardRepository.getDashboardDesigns(any(), any(), any())) doReturn getErrorFlow()

      viewModel.refreshFetch()

      verify(authSharedPrefRepository).userId
      verify(dashboardRepository).getDashboardDesigns(idCaptor.capture(), pageCaptor.capture(),
          pageCaptor.capture())
      assertEquals(idCaptor.firstValue, TAILOR_ID)
      assertEquals(pageCaptor.firstValue, PAGE)
      assertEquals(pageCaptor.secondValue, ITEM_PER_PAGE)
      delay(1000)

      assertError(Constants.FAILED_TO_GET_YOUR_DESIGN)

      verifyNoMoreInteractions(authSharedPrefRepository, dashboardRepository)
    }
  }

  @Test
  fun `Given when fetch tailor designs and user id is null then do nothing`() {
    rule.dispatcher.runBlockingTest {
      whenever(authSharedPrefRepository.userId) doReturn null

      viewModel.fetchMore()

      verify(authSharedPrefRepository).userId

      verifyZeroInteractions(dashboardRepository)
      verifyNoMoreInteractions(authSharedPrefRepository)
    }
  }

  @Test
  fun `Given when delete tailor design by id then success update live data`() {
    rule.dispatcher.runBlockingTest {
      val flow = getFlow(BaseResponse())
      whenever(authSharedPrefRepository.userId) doReturn TAILOR_ID
      whenever(dashboardRepository.deleteDashboardDesign(any(), any())) doReturn flow

      viewModel.deleteDesign(DESIGN_ID)

      verify(authSharedPrefRepository).userId
      verify(dashboardRepository).deleteDashboardDesign(idCaptor.capture(), idCaptor.capture())
      assertEquals(idCaptor.firstValue, TAILOR_ID)
      assertEquals(idCaptor.secondValue, DESIGN_ID)
      delay(1000)

      viewModel.hasBeenDeleted.observeForTesting {
        assertEquals(viewModel.hasBeenDeleted.value, true)
      }

      verifyNoMoreInteractions(authSharedPrefRepository, dashboardRepository)
    }
  }

  @Test
  fun `Given when delete tailor design by id and error then update error message`() {
    rule.dispatcher.runBlockingTest {
      whenever(authSharedPrefRepository.userId) doReturn TAILOR_ID
      whenever(dashboardRepository.deleteDashboardDesign(any(), any())) doReturn getErrorFlow()

      viewModel.deleteDesign(DESIGN_ID)

      verify(authSharedPrefRepository).userId
      verify(dashboardRepository).deleteDashboardDesign(idCaptor.capture(), idCaptor.capture())
      assertEquals(idCaptor.firstValue, TAILOR_ID)
      assertEquals(idCaptor.secondValue, DESIGN_ID)
      delay(1000)

      assertError(Constants.FAILED_TO_DELETE_DESIGN)
      viewModel.hasBeenDeleted.observeForTesting {
        assertEquals(viewModel.hasBeenDeleted.value, false)
      }

      verifyNoMoreInteractions(authSharedPrefRepository, dashboardRepository)
    }
  }

  @Test
  fun `Given when delete tailor design by id and user id is null then do nothing`() {
    rule.dispatcher.runBlockingTest {
      whenever(authSharedPrefRepository.userId) doReturn null

      viewModel.deleteDesign(DESIGN_ID)

      verify(authSharedPrefRepository).userId

      verifyZeroInteractions(dashboardRepository)
      verifyNoMoreInteractions(authSharedPrefRepository)
    }
  }

  private fun assertError(message: String) {
    viewModel.errorMessage.observeForTesting {
      assertEquals(viewModel.errorMessage.value, message)
    }
  }
}