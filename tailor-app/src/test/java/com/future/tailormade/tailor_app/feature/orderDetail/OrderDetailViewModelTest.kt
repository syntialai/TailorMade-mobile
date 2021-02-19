package com.future.tailormade.tailor_app.feature.orderDetail

import com.future.tailormade.config.Constants
import com.future.tailormade.tailor_app.base.BaseViewModelTest
import com.future.tailormade.tailor_app.base.PayloadMapper
import com.future.tailormade.tailor_app.core.repository.OrderRepository
import com.future.tailormade.tailor_app.feature.orderDetail.viewModel.OrderDetailViewModel
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
class OrderDetailViewModelTest : BaseViewModelTest() {
  
  private lateinit var viewModel: OrderDetailViewModel
  
  private val orderRepository = mock<OrderRepository>()

  @Before
  override fun setUp() {
    MockitoAnnotations.openMocks(this)
    viewModel = OrderDetailViewModel(orderRepository, authSharedPrefRepository, savedStateHandle)
  }

  @After
  override fun tearDown() {
    Mockito.framework().clearInlineMocks()
  }
  
  @Test
  fun `Given when fetch history detail then success update live data`() {
    val expectedUiModel = PayloadMapper.getOrderDetailUiModel()

    rule.dispatcher.runBlockingTest {
      val flow = getFlow(expectedUiModel)
      whenever(authSharedPrefRepository.userId) doReturn TAILOR_ID
      whenever(orderRepository.getOrderDetail(TAILOR_ID, ORDER_ID)) doReturn flow

      viewModel.fetchOrderDetail(ORDER_ID)

      verify(authSharedPrefRepository).userId
      verify(orderRepository).getOrderDetail(TAILOR_ID, ORDER_ID)
      delay(1000)

      viewModel.orderDetailUiModel.observeForTesting {
        assertEquals(viewModel.orderDetailUiModel.value, expectedUiModel)
      }
      verifyNoMoreInteractions(orderRepository, authSharedPrefRepository)
    }
  }

  @Test
  fun `Given when fetch history detail and error then update error message`() {
    rule.dispatcher.runBlockingTest {
      whenever(authSharedPrefRepository.userId) doReturn TAILOR_ID
      whenever(orderRepository.getOrderDetail(TAILOR_ID, ORDER_ID)) doReturn getErrorFlow()

      viewModel.fetchOrderDetail(ORDER_ID)

      verify(authSharedPrefRepository).userId
      verify(orderRepository).getOrderDetail(TAILOR_ID, ORDER_ID)
      delay(1000)

      viewModel.errorMessage.observeForTesting {
        assertEquals(viewModel.errorMessage.value, Constants.FAILED_TO_FETCH_ORDER_DETAIL)
      }
      verifyNoMoreInteractions(orderRepository, authSharedPrefRepository)
    }
  }

  @Test
  fun `Given when fetch history detail and user id is null then do nothing`() {
    rule.dispatcher.runBlockingTest {
      whenever(authSharedPrefRepository.userId) doReturn null

      viewModel.fetchOrderDetail(ORDER_ID)

      verify(authSharedPrefRepository).userId
      delay(1000)

      verifyZeroInteractions(orderRepository)
      verifyNoMoreInteractions(authSharedPrefRepository)
    }
  }
}