package com.future.tailormade.core.repository.impl

import com.future.tailormade.base.repository.BaseRepository
import com.future.tailormade.core.mapper.OrderMapper
import com.future.tailormade.core.model.response.history.OrderDesignResponse
import com.future.tailormade.core.model.response.history.OrderResponse
import com.future.tailormade.core.repository.OrderRepository
import com.future.tailormade.core.service.OrderService
import com.future.tailormade.util.extension.flowOnIO
import javax.inject.Inject
import kotlinx.coroutines.flow.flow

class OrderRepositoryImpl @Inject constructor(private val orderService: OrderService) :
    BaseRepository(), OrderRepository {

  override fun getLogName() = "com.future.tailormade.core.repository.impl.OrderRepositoryImpl"

  override suspend fun getOrders(userId: String, page: Int, itemPerPage: Int) = flow {
//    val orders = orderService.getUserOrders(userId, page, itemPerPage).data
//    emit(orders?.map {
//      OrderMapper.mapToHistoryUiModel(it)
//    } as ArrayList)
    val orderResponse = OrderResponse(0,
        OrderDesignResponse("", 0.0, "", "", 0.0, "", "", "", "").getMockResponse(), "", 1, "", "",
        0.0, 0.0, 0, "").getMockResponse()
    emit(arrayListOf(OrderMapper.mapToHistoryUiModel(orderResponse)))
  }.flowOnIO()

  override suspend fun getOrderDetail(userId: String, id: String) = flow {
    val orderDetail = orderService.getUserOrdersByOrderId(userId, id).data
    orderDetail?.let {
      emit(OrderMapper.mapToHistoryDetailUiModel(it))
    }
  }.flowOnIO()
}