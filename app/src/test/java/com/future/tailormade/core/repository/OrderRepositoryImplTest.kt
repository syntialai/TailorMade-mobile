package com.future.tailormade.core.repository

import com.future.tailormade.base.test.BaseTest
import com.future.tailormade.core.mapper.OrderMapper
import com.future.tailormade.core.model.response.history.OrderDesignResponse
import com.future.tailormade.core.model.response.history.OrderDetailMeasurementResponse
import com.future.tailormade.core.model.response.history.OrderDetailResponse
import com.future.tailormade.core.model.response.history.OrderResponse
import com.future.tailormade.core.repository.impl.OrderRepositoryImpl
import com.future.tailormade.core.service.OrderService
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
class OrderRepositoryImplTest: BaseTest() {

  companion object {
    private const val USER_ID = "USER_1"
    private const val ID = "ORDER_1"
    private const val PAGE = 1
    private const val ITEM_PER_PAGE = 10
  }

  @InjectMocks
  private lateinit var orderRepository: OrderRepositoryImpl

  @Mock
  private lateinit var orderService: OrderService

  @Before
  override fun setUp() {
    MockitoAnnotations.openMocks(this)
  }

  @After
  override fun tearDown() {
    Mockito.framework().clearInlineMocks()
  }

  @Test
  fun `Given when get orders then success return mapped response`() {
    val expectedResponse = generateListBaseResponse(data = getOrdersResponse())
    val expectedUiModel = expectedResponse.data!!.map {
      OrderMapper.mapToHistoryUiModel(it)
    } as ArrayList

    runBlocking {
      orderService.stub {
        onBlocking { getUserOrders(USER_ID, PAGE, ITEM_PER_PAGE) } doReturn expectedResponse
      }

      val flow = orderRepository.getOrders(USER_ID, PAGE, ITEM_PER_PAGE)

      flow.collect {
        Mockito.verify(orderService).getUserOrders(USER_ID, PAGE, ITEM_PER_PAGE)
        assertEquals(it, expectedUiModel)
      }
    }
  }

  @Test
  fun `Given when get order detail then success return mapped response`() {
    val expectedResponse = generateSingleBaseResponse(data = getOrderDetailResponse())
    val expectedUiModel = OrderMapper.mapToHistoryDetailUiModel(expectedResponse.data!!)

    runBlocking {
      orderService.stub {
        onBlocking { getUserOrdersByOrderId(USER_ID, ID) } doReturn expectedResponse
      }

      val flow = orderRepository.getOrderDetail(USER_ID, ID)

      flow.collect {
        Mockito.verify(orderService).getUserOrdersByOrderId(USER_ID, ID)
        assertEquals(it, expectedUiModel)
      }
    }
  }

  private fun getOrdersResponse() = listOf(
      OrderResponse(0, getOrderDesignResponse(), ID, 1, "", "", 0.0, 0.0, 0, USER_ID))

  private fun getOrderDetailResponse() = OrderDetailResponse(0, getOrderDesignResponse(), ID,
      getOrderDetailMeasurementResponse(), 1, "", "", "", "", 0.0, 0.0, 0, USER_ID, "")

  private fun getOrderDesignResponse() = OrderDesignResponse(
      "", 0.0, "", "", 0.0, "", "", "", "").getMockResponse()

  private fun getOrderDetailMeasurementResponse() = OrderDetailMeasurementResponse(0f, 0f, 0f, 0f,
      0f).getMockResponse()
}