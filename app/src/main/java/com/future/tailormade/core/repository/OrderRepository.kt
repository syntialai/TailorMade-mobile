package com.future.tailormade.core.repository

import com.future.tailormade.core.model.ui.history.OrderDetailUiModel
import com.future.tailormade.core.model.ui.history.OrderUiModel
import kotlinx.coroutines.flow.Flow

interface OrderRepository {

  suspend fun getOrders(userId: String, page: Int, itemPerPage: Int): Flow<ArrayList<OrderUiModel>>

  suspend fun getOrderDetail(userId: String, id: String): Flow<OrderDetailUiModel>
}