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

  override suspend fun getOrders(tailorId: String, status: String) = flow {
    val response = orderService.getTailorOrders(tailorId, status).data
    response?.let { data ->
      emit(data.map { OrderMapper.mapToOrderUiModel(it) } as ArrayList)
    }
  }.flowOnIO()
}