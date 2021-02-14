package com.future.tailormade.feature.history

import com.future.tailormade.base.BaseViewModelTest
import com.future.tailormade.base.PayloadMapper
import com.future.tailormade.config.Constants
import com.future.tailormade.core.repository.OrderRepository
import com.future.tailormade.feature.history.viewModel.HistoryDetailViewModel
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
class HistoryDetailViewModelTest : BaseViewModelTest() {
  
  private lateinit var viewModel: HistoryDetailViewModel
  
  private val orderRepository = mock<OrderRepository>()

  @Before
  override fun setUp() {
    MockitoAnnotations.openMocks(this)
    viewModel = HistoryDetailViewModel(orderRepository, authSharedPrefRepository, savedStateHandle)
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
      whenever(authSharedPrefRepository.userId) doReturn USER_ID
      whenever(orderRepository.getOrderDetail(USER_ID, ORDER_ID)) doReturn flow

      viewModel.fetchHistoryDetails(ORDER_ID)

      verify(authSharedPrefRepository).userId
      verify(orderRepository).getOrderDetail(USER_ID, ORDER_ID)
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
      whenever(authSharedPrefRepository.userId) doReturn USER_ID
      whenever(orderRepository.getOrderDetail(USER_ID, ORDER_ID)) doReturn getErrorFlow()

      viewModel.fetchHistoryDetails(ORDER_ID)

      verify(authSharedPrefRepository).userId
      verify(orderRepository).getOrderDetail(USER_ID, ORDER_ID)
      delay(1000)

      viewModel.errorMessage.observeForTesting {
        assertEquals(viewModel.errorMessage.value,
            Constants.generateFailedFetchError("history with id $ORDER_ID"))
      }
      verifyNoMoreInteractions(orderRepository, authSharedPrefRepository)
    }
  }

  @Test
  fun `Given when fetch history detail and user id is null then do nothing`() {
    rule.dispatcher.runBlockingTest {
      whenever(authSharedPrefRepository.userId) doReturn null

      viewModel.fetchHistoryDetails(ORDER_ID)

      verify(authSharedPrefRepository).userId
      delay(1000)

      verifyZeroInteractions(orderRepository)
      verifyNoMoreInteractions(authSharedPrefRepository)
    }
  }
}