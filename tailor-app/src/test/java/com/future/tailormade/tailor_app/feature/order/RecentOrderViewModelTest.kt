package com.future.tailormade.tailor_app.feature.order

import com.future.tailormade.config.Constants
import com.future.tailormade.tailor_app.base.BaseViewModelTest
import com.future.tailormade.tailor_app.base.PayloadMapper
import com.future.tailormade.tailor_app.core.model.enums.OrderStatus
import com.future.tailormade.tailor_app.core.repository.OrderRepository
import com.future.tailormade.tailor_app.feature.order.viewModel.RecentOrderViewModel
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
class RecentOrderViewModelTest : BaseViewModelTest() {
  
  private lateinit var viewModel: RecentOrderViewModel
  
  private val orderRepository = mock<OrderRepository>()

  @Before
  override fun setUp() {
    MockitoAnnotations.openMocks(this)
    viewModel = RecentOrderViewModel(orderRepository, authSharedPrefRepository, savedStateHandle)
  }

  @After
  override fun tearDown() {
    Mockito.framework().clearInlineMocks()
  }
  
  @Test
  fun `Given when fetch accepted orders then success update live data`() {
    val expectedUiModel = PayloadMapper.getOrdersUiModel()

    rule.dispatcher.runBlockingTest {
      val flow = getFlow(expectedUiModel)
      whenever(authSharedPrefRepository.userId) doReturn TAILOR_ID
      whenever(orderRepository.getOrders(
          TAILOR_ID, OrderStatus.Accepted.name, PAGE, ITEM_PER_PAGE)) doReturn flow

      viewModel.fetchAcceptedOrders()

      verify(authSharedPrefRepository).userId
      verify(orderRepository).getOrders(TAILOR_ID, OrderStatus.Accepted.name, PAGE, ITEM_PER_PAGE)
      delay(1000)

      viewModel.acceptedOrders.observeForTesting {
        assertEquals(viewModel.acceptedOrders.value, expectedUiModel)
      }
      verifyNoMoreInteractions(orderRepository, authSharedPrefRepository)
    }
  }

  @Test
  fun `Given when fetch rejected orders then success update live data`() {
    val expectedUiModel = PayloadMapper.getOrdersUiModel()

    rule.dispatcher.runBlockingTest {
      val flow = getFlow(expectedUiModel)
      whenever(authSharedPrefRepository.userId) doReturn TAILOR_ID
      whenever(orderRepository.getOrders(
          TAILOR_ID, OrderStatus.Rejected.name, PAGE, ITEM_PER_PAGE)) doReturn flow

      viewModel.fetchRejectedOrders()

      verify(authSharedPrefRepository).userId
      verify(orderRepository).getOrders(TAILOR_ID, OrderStatus.Rejected.name, PAGE, ITEM_PER_PAGE)
      delay(1000)

      viewModel.rejectedOrders.observeForTesting {
        assertEquals(viewModel.rejectedOrders.value, expectedUiModel)
      }
      verifyNoMoreInteractions(orderRepository, authSharedPrefRepository)
    }
  }

  @Test
  fun `Given when fetch accepted orders and error then update error message`() {
    rule.dispatcher.runBlockingTest {
      whenever(authSharedPrefRepository.userId) doReturn TAILOR_ID
      whenever(orderRepository.getOrders(
          TAILOR_ID, OrderStatus.Accepted.name, PAGE, ITEM_PER_PAGE)) doReturn getErrorFlow()

      viewModel.fetchAcceptedOrders()

      verify(authSharedPrefRepository).userId
      verify(orderRepository).getOrders(TAILOR_ID, OrderStatus.Accepted.name, PAGE, ITEM_PER_PAGE)
      delay(1000)

      assertError()

      verifyNoMoreInteractions(orderRepository, authSharedPrefRepository)
    }
  }

  @Test
  fun `Given when fetch recent orders and user id is null then do nothing`() {
    rule.dispatcher.runBlockingTest {
      whenever(authSharedPrefRepository.userId) doReturn null

      viewModel.fetchRejectedOrders()

      verify(authSharedPrefRepository).userId
      delay(1000)

      verifyZeroInteractions(orderRepository)
      verifyNoMoreInteractions(authSharedPrefRepository)
    }
  }

  private fun assertError() {
    viewModel.errorMessage.observeForTesting {
      assertEquals(viewModel.errorMessage.value, Constants.FAILED_TO_FETCH_RECENT_ORDER)
    }
  }
}