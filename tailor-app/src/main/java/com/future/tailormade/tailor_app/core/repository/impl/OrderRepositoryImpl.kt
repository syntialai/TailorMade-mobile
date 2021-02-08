package com.future.tailormade.tailor_app.core.repository.impl

import com.future.tailormade.base.repository.BaseRepository
import com.future.tailormade.tailor_app.core.mapper.OrderMapper
import com.future.tailormade.tailor_app.core.repository.OrderRepository
import com.future.tailormade.tailor_app.core.service.OrderService
import com.future.tailormade.util.extension.flowOnIO
import javax.inject.Inject
import kotlinx.coroutines.flow.flow

class OrderRepositoryImpl @Inject constructor(private val orderService: OrderService) :
    BaseRepository(), OrderRepository {

  override fun getLogName() =
      "com.future.tailormade.tailor_app.core.repository.impl.OrderRepositoryImpl"

  override suspend fun getOrders(tailorId: String, status: String, page: Int, itemPerPage: Int) = flow {
    val response = orderService.getTailorOrders(tailorId, status, page, itemPerPage).data
    response?.let { data ->
      emit(data.map { OrderMapper.mapToOrderUiModel(it) } as ArrayList)
    }
//    emit(DataMock.getOrdersMock())
  }.flowOnIO()

  override suspend fun getOrderDetail(tailorId: String, id: String) = flow {
    val response = orderService.getTailorOrderById(tailorId, id).data
    response?.let { data ->
      emit(OrderMapper.mapToOrderDetailUiModel(data))
    }
//    emit(DataMock.getOrderDetailMock())
  }.flowOnIO()

  override suspend fun acceptOrder(tailorId: String, id: String) = flow {
    emit(orderService.putAcceptOrder(tailorId, id))
  }.flowOnIO()

  override suspend fun rejectOrder(tailorId: String, id: String) = flow {
    emit(orderService.putRejectOrder(tailorId, id))
  }.flowOnIO()
}