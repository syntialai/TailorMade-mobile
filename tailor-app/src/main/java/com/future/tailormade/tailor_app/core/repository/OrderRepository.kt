package com.future.tailormade.tailor_app.core.repository

import com.future.tailormade.tailor_app.core.model.ui.order.OrderUiModel
import kotlinx.coroutines.flow.Flow

interface OrderRepository {

  suspend fun getOrders(tailorId: String, status: String): Flow<ArrayList<OrderUiModel>>
}