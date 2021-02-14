package com.future.tailormade.feature.history

import com.future.tailormade.base.BaseViewModelTest
import com.future.tailormade.base.PayloadMapper
import com.future.tailormade.config.Constants
import com.future.tailormade.core.repository.OrderRepository
import com.future.tailormade.feature.history.viewModel.HistoryViewModel
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
class HistoryViewModelTest : BaseViewModelTest() {
  
  private lateinit var viewModel: HistoryViewModel
  
  private val orderRepository = mock<OrderRepository>()

  @Before
  override fun setUp() {
    MockitoAnnotations.openMocks(this)
    viewModel = HistoryViewModel(orderRepository, authSharedPrefRepository)
  }

  @After
  override fun tearDown() {
    Mockito.framework().clearInlineMocks()
  }
  
  @Test
  fun `Given when fetch history then success update live data`() {
    val expectedUiModel = PayloadMapper.getOrdersUiModel()

    rule.dispatcher.runBlockingTest {
      val flow = getFlow(expectedUiModel)
      whenever(authSharedPrefRepository.userId) doReturn USER_ID
      whenever(orderRepository.getOrders(USER_ID, PAGE, ITEM_PER_PAGE)) doReturn flow

      viewModel.fetchHistory()

      verify(authSharedPrefRepository).userId
      verify(orderRepository).getOrders(USER_ID, PAGE, ITEM_PER_PAGE)
      delay(1000)

      viewModel.orders.observeForTesting {
        assertEquals(viewModel.orders.value, expectedUiModel)
      }
      verifyNoMoreInteractions(orderRepository, authSharedPrefRepository)
    }
  }

  @Test
  fun `Given when fetch history and error then update error message`() {
    rule.dispatcher.runBlockingTest {
      whenever(authSharedPrefRepository.userId) doReturn USER_ID
      whenever(orderRepository.getOrders(USER_ID, PAGE, ITEM_PER_PAGE)) doReturn getErrorFlow()

      viewModel.refreshFetch()

      verify(authSharedPrefRepository).userId
      verify(orderRepository).getOrders(USER_ID, PAGE, ITEM_PER_PAGE)
      delay(1000)

      viewModel.errorMessage.observeForTesting {
        assertEquals(viewModel.errorMessage.value, Constants.generateFailedFetchError("history"))
      }
      verifyNoMoreInteractions(orderRepository, authSharedPrefRepository)
    }
  }

  @Test
  fun `Given when fetch history and user id is null then do nothing`() {
    rule.dispatcher.runBlockingTest {
      whenever(authSharedPrefRepository.userId) doReturn null

      viewModel.fetchMore()

      verify(authSharedPrefRepository).userId
      delay(1000)

      verifyZeroInteractions(orderRepository)
      verifyNoMoreInteractions(authSharedPrefRepository)
    }
  }
}