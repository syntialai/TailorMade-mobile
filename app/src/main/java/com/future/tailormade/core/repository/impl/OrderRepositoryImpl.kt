package com.future.tailormade.core.repository.impl

import com.future.tailormade.base.repository.BaseRepository
import com.future.tailormade.core.repository.OrderRepository
import com.future.tailormade.core.service.OrderService
import com.future.tailormade.util.extension.flowOnIO
import javax.inject.Inject
import kotlinx.coroutines.flow.flow

class OrderRepositoryImpl @Inject constructor(private val orderService: OrderService) :
		BaseRepository(), OrderRepository {

	override fun getLogName() = "com.future.tailormade.core.repository.impl.OrderRepositoryImpl"

	override suspend fun getOrders(userId: String) = flow {
		emit(orderService.getUserOrders(userId))
	}.flowOnIO()

	override suspend fun getOrderDetail(userId: String, id: String) = flow {
		emit(orderService.getUserOrdersByOrderId(userId, id))
	}.flowOnIO()
}