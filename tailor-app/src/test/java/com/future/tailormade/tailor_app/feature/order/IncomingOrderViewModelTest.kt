package com.future.tailormade.tailor_app.feature.order

import com.future.tailormade.base.model.response.BaseResponse
import com.future.tailormade.config.Constants
import com.future.tailormade.tailor_app.base.BaseViewModelTest
import com.future.tailormade.tailor_app.base.PayloadMapper
import com.future.tailormade.tailor_app.core.model.enums.OrderStatus
import com.future.tailormade.tailor_app.core.repository.OrderRepository
import com.future.tailormade.tailor_app.feature.order.viewModel.IncomingOrderViewModel
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
class IncomingOrderViewModelTest : BaseViewModelTest() {
  
  private lateinit var viewModel: IncomingOrderViewModel
  
  private val orderRepository = mock<OrderRepository>()

  @Before
  override fun setUp() {
    MockitoAnnotations.openMocks(this)
    viewModel = IncomingOrderViewModel(orderRepository, authSharedPrefRepository, savedStateHandle)
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
      whenever(authSharedPrefRepository.userId) doReturn TAILOR_ID
      whenever(orderRepository.getOrders(TAILOR_ID, OrderStatus.Incoming.name, PAGE, ITEM_PER_PAGE)) doReturn flow

      viewModel.fetchIncomingOrders()

      verify(authSharedPrefRepository).userId
      verify(orderRepository).getOrders(TAILOR_ID, OrderStatus.Incoming.name, PAGE, ITEM_PER_PAGE)
      delay(1000)

      viewModel.incomingOrders.observeForTesting {
        assertEquals(viewModel.incomingOrders.value, expectedUiModel)
      }
      verifyNoMoreInteractions(orderRepository, authSharedPrefRepository)
    }
  }

  @Test
  fun `Given when fetch history and error then update error message`() {
    rule.dispatcher.runBlockingTest {
      whenever(authSharedPrefRepository.userId) doReturn TAILOR_ID
      whenever(orderRepository.getOrders(TAILOR_ID, OrderStatus.Incoming.name, PAGE, ITEM_PER_PAGE)) doReturn getErrorFlow()

      viewModel.fetchIncomingOrders()

      verify(authSharedPrefRepository).userId
      verify(orderRepository).getOrders(TAILOR_ID, OrderStatus.Incoming.name, PAGE, ITEM_PER_PAGE)
      delay(1000)

      assertError(Constants.FAILED_TO_FETCH_INCOMING_ORDER)

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

  @Test
  fun `Given when accept order then success update live data`() {
    rule.dispatcher.runBlockingTest {
      whenever(authSharedPrefRepository.userId) doReturn TAILOR_ID
      whenever(orderRepository.acceptOrder(TAILOR_ID, ORDER_ID)) doReturn getFlow(BaseResponse())

      viewModel.acceptOrder(ORDER_ID)

      verify(authSharedPrefRepository).userId
      verify(orderRepository).acceptOrder(TAILOR_ID, ORDER_ID)
      assertLoading(true)
      delay(1000)

      assertLoading(false)
      assertHasResponded(Pair(Constants.STATUS_ACCEPTED, true))

      verifyNoMoreInteractions(orderRepository, authSharedPrefRepository)
    }
  }

  @Test
  fun `Given when accept order and error then update error message`() {
    rule.dispatcher.runBlockingTest {
      whenever(authSharedPrefRepository.userId) doReturn TAILOR_ID
      whenever(orderRepository.acceptOrder(TAILOR_ID, ORDER_ID)) doReturn getErrorFlow()

      viewModel.acceptOrder(ORDER_ID)

      verify(authSharedPrefRepository).userId
      verify(orderRepository).acceptOrder(TAILOR_ID, ORDER_ID)
      assertLoading(true)
      delay(1000)

      assertLoading(false)
      assertError(Constants.FAILED_TO_ACCEPT_ORDER)
      assertHasResponded(Pair(Constants.STATUS_ACCEPTED, false))

      verifyNoMoreInteractions(orderRepository, authSharedPrefRepository)
    }
  }

  @Test
  fun `Given when accept order and user id is null then do nothing`() {
    rule.dispatcher.runBlockingTest {
      whenever(authSharedPrefRepository.userId) doReturn null

      viewModel.acceptOrder(ORDER_ID)

      verify(authSharedPrefRepository).userId

      verifyZeroInteractions(orderRepository)
      verifyNoMoreInteractions(authSharedPrefRepository)
    }
  }

  @Test
  fun `Given when reject order then success update live data`() {
    rule.dispatcher.runBlockingTest {
      whenever(authSharedPrefRepository.userId) doReturn TAILOR_ID
      whenever(orderRepository.rejectOrder(TAILOR_ID, ORDER_ID)) doReturn getFlow(BaseResponse())

      viewModel.rejectOrder(ORDER_ID)

      verify(authSharedPrefRepository).userId
      verify(orderRepository).rejectOrder(TAILOR_ID, ORDER_ID)
      assertLoading(true)
      delay(1000)

      assertLoading(false)
      assertHasResponded(Pair(Constants.STATUS_REJECTED, true))

      verifyNoMoreInteractions(orderRepository, authSharedPrefRepository)
    }
  }

  @Test
  fun `Given when reject order and error then update error message`() {
    rule.dispatcher.runBlockingTest {
      whenever(authSharedPrefRepository.userId) doReturn TAILOR_ID
      whenever(orderRepository.rejectOrder(TAILOR_ID, ORDER_ID)) doReturn getErrorFlow()

      viewModel.rejectOrder(ORDER_ID)

      verify(authSharedPrefRepository).userId
      verify(orderRepository).rejectOrder(TAILOR_ID, ORDER_ID)
      assertLoading(true)
      delay(1000)

      assertLoading(false)
      assertError(Constants.FAILED_TO_REJECT_ORDER)
      assertHasResponded(Pair(Constants.STATUS_REJECTED, false))

      verifyNoMoreInteractions(orderRepository, authSharedPrefRepository)
    }
  }

  @Test
  fun `Given when reject order and user id is null then do nothing`() {
    rule.dispatcher.runBlockingTest {
      whenever(authSharedPrefRepository.userId) doReturn null

      viewModel.rejectOrder(ORDER_ID)

      verify(authSharedPrefRepository).userId

      verifyZeroInteractions(orderRepository)
      verifyNoMoreInteractions(authSharedPrefRepository)
    }
  }

  private fun assertError(message: String) {
    viewModel.errorMessage.observeForTesting {
      assertEquals(viewModel.errorMessage.value, message)
    }
  }

  private fun assertHasResponded(hasResponded: Pair<String, Boolean>?) {
    viewModel.hasOrderResponded.observeForTesting {
      assertEquals(viewModel.hasOrderResponded.value, hasResponded)
    }
  }

  private fun assertLoading(isLoading: Boolean) {
    viewModel.isLoading.observeForTesting {
      assertEquals(viewModel.isLoading.value, isLoading)
    }
  }
}