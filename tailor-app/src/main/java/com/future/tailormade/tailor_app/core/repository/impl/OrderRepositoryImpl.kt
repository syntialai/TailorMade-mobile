package com.future.tailormade.tailor_app.core.repository.impl

import com.future.tailormade.base.repository.BaseRepository
import com.future.tailormade.tailor_app.core.mapper.OrderMapper
import com.future.tailormade.tailor_app.core.repository.OrderRepository
import com.future.tailormade.tailor_app.core.service.OrderService
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class OrderRepositoryImpl @Inject constructor(private val orderService: OrderService,
    private val ioDispatcher: CoroutineDispatcher) : BaseRepository(), OrderRepository {

  override fun getLogName() =
      "com.future.tailormade.tailor_app.core.repository.impl.OrderRepositoryImpl"

  override suspend fun getOrders(tailorId: String, status: String, page: Int, itemPerPage: Int) = flow {
    orderService.getTailorOrders(tailorId, status, page, itemPerPage).data?.let { data ->
      emit(data.map {
        OrderMapper.mapToOrderUiModel(it)
      } as ArrayList)
    }
  }.flowOn(ioDispatcher)

  override suspend fun getOrderDetail(tailorId: String, id: String) = flow {
    orderService.getTailorOrderById(tailorId, id).data?.let { data ->
      emit(OrderMapper.mapToOrderDetailUiModel(data))
    }
  }.flowOn(ioDispatcher)

  override suspend fun acceptOrder(tailorId: String, id: String) = flow {
    emit(orderService.putAcceptOrder(tailorId, id))
  }.flowOn(ioDispatcher)

  override suspend fun rejectOrder(tailorId: String, id: String) = flow {
    emit(orderService.putRejectOrder(tailorId, id))
  }.flowOn(ioDispatcher)
}