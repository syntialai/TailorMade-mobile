package com.future.tailormade.tailor_app.core.repository

import com.future.tailormade.base.test.BaseTest
import com.future.tailormade.config.Constants
import com.future.tailormade.tailor_app.base.PayloadMapper
import com.future.tailormade.tailor_app.core.repository.impl.OrderRepositoryImpl
import com.future.tailormade.tailor_app.core.service.OrderService
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.stub
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
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
class OrderRepositoryImplTest : BaseTest() {

  private lateinit var orderRepository: OrderRepository

  @Mock
  private lateinit var orderService: OrderService

  private val dispatcher = TestCoroutineDispatcher()

  private val idCaptor = argumentCaptor<String>()

  private val objectCaptor = argumentCaptor<Any>()

  @Before
  override fun setUp() {
    MockitoAnnotations.openMocks(this)
    orderRepository = OrderRepositoryImpl(orderService, dispatcher)
  }

  @After
  override fun tearDown() {
    Mockito.framework().clearInlineMocks()
  }

  @Test
  fun `Given when get orders then success return mapped response`() {
    val expectedResponse = generateListBaseResponse(data = PayloadMapper.getOrdersResponse())
    val expectedUiModel = PayloadMapper.getOrdersUiModel()

    dispatcher.runBlockingTest {
      orderService.stub {
        onBlocking {
          getTailorOrders(TAILOR_ID, Constants.STATUS_ACCEPTED, PAGE, ITEM_PER_PAGE)
        } doReturn expectedResponse
      }

      val flow = orderRepository.getOrders(TAILOR_ID, Constants.STATUS_ACCEPTED, PAGE,
          ITEM_PER_PAGE)

      flow.collect {
        Mockito.verify(orderService).getTailorOrders(TAILOR_ID, Constants.STATUS_ACCEPTED, PAGE, ITEM_PER_PAGE)
        assertEquals(it, expectedUiModel)
        verifyNoMoreInteractions(orderService)
      }
    }
  }

  @Test
  fun `Given when get orders and data returned is null then do nothing`() {
    dispatcher.runBlockingTest {
      orderService.stub {
        onBlocking {
          getTailorOrders(TAILOR_ID, Constants.STATUS_ACCEPTED, PAGE, ITEM_PER_PAGE)
        } doReturn generateListBaseResponse()
      }

      val flow = orderRepository.getOrders(TAILOR_ID, Constants.STATUS_ACCEPTED, PAGE,
          ITEM_PER_PAGE)

      flow.collect {
        Mockito.verify(orderService).getTailorOrders(TAILOR_ID, Constants.STATUS_ACCEPTED, PAGE, ITEM_PER_PAGE)
        verifyNoMoreInteractions(orderService)
      }
    }
  }

  @Test
  fun `Given when get order detail then success return mapped response`() {
    val expectedResponse = generateSingleBaseResponse(data = PayloadMapper.getOrderDetailResponse())
    val expectedUiModel = PayloadMapper.getOrderDetailUiModel()

    dispatcher.runBlockingTest {
      orderService.stub {
        onBlocking { getTailorOrderById(TAILOR_ID, ORDER_ID) } doReturn expectedResponse
      }

      val flow = orderRepository.getOrderDetail(TAILOR_ID, ORDER_ID)

      flow.collect {
        Mockito.verify(orderService).getTailorOrderById(TAILOR_ID, ORDER_ID)
        assertEquals(it, expectedUiModel)
        verifyNoMoreInteractions(orderService)
      }
    }
  }

  @Test
  fun `Given when get order detail and data returned is null then do nothing`() {
    dispatcher.runBlockingTest {
      orderService.stub {
        onBlocking { getTailorOrderById(TAILOR_ID, ORDER_ID) } doReturn generateSingleBaseResponse()
      }

      val flow = orderRepository.getOrderDetail(TAILOR_ID, ORDER_ID)

      flow.collect {
        Mockito.verify(orderService).getTailorOrderById(TAILOR_ID, ORDER_ID)
        verifyNoMoreInteractions(orderService)
      }
    }
  }

  @Test
  fun `Given when accept order then success return response`() {
    val expectedResponse = generateBaseResponse(CODE_OK, STATUS_OK)

    dispatcher.runBlockingTest {
      orderService.stub {
        onBlocking { putAcceptOrder(any(), any(), any()) } doReturn expectedResponse
      }

      val flow = orderRepository.acceptOrder(TAILOR_ID, ORDER_ID)

      flow.collect {
        Mockito.verify(orderService).putAcceptOrder(idCaptor.capture(), idCaptor.capture(),
            objectCaptor.capture())
        assertEquals(idCaptor.firstValue, TAILOR_ID)
        assertEquals(idCaptor.secondValue, ORDER_ID)
        assertNotNull(objectCaptor.firstValue)
        verifyNoMoreInteractions(orderService)
      }
    }
  }

  @Test
  fun `Given when reject order then success return response`() {
    val expectedResponse = generateBaseResponse(CODE_OK, STATUS_OK)

    dispatcher.runBlockingTest {
      orderService.stub {
        onBlocking { putRejectOrder(any(), any(), any()) } doReturn expectedResponse
      }

      val flow = orderRepository.rejectOrder(TAILOR_ID, ORDER_ID)

      flow.collect {
        Mockito.verify(orderService).putRejectOrder(idCaptor.capture(), idCaptor.capture(),
            objectCaptor.capture())
        assertEquals(idCaptor.firstValue, TAILOR_ID)
        assertEquals(idCaptor.secondValue, ORDER_ID)
        assertNotNull(objectCaptor.firstValue)
        verifyNoMoreInteractions(orderService)
      }
    }
  }
}