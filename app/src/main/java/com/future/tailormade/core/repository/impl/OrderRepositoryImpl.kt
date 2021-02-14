package com.future.tailormade.core.repository.impl

import com.future.tailormade.base.repository.BaseRepository
import com.future.tailormade.core.mapper.OrderMapper
import com.future.tailormade.core.repository.OrderRepository
import com.future.tailormade.core.service.OrderService
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class OrderRepositoryImpl @Inject constructor(private val orderService: OrderService,
    private val ioDispatcher: CoroutineDispatcher) : BaseRepository(), OrderRepository {

  override fun getLogName() = "com.future.tailormade.core.repository.impl.OrderRepositoryImpl"

  override suspend fun getOrders(userId: String, page: Int, itemPerPage: Int) = flow {
    orderService.getUserOrders(userId, page, itemPerPage).data?.let { orders ->
      emit(orders.map {
        OrderMapper.mapToHistoryUiModel(it)
      } as ArrayList)
    }
  }.flowOn(ioDispatcher)

  override suspend fun getOrderDetail(userId: String, id: String) = flow {
    orderService.getUserOrdersByOrderId(userId, id).data?.let { orderDetail ->
      emit(OrderMapper.mapToHistoryDetailUiModel(orderDetail))
    }
  }.flowOn(ioDispatcher)
}