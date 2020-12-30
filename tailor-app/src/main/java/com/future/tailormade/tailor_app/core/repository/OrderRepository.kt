package com.future.tailormade.tailor_app.core.repository

import com.future.tailormade.base.model.response.BaseResponse
import com.future.tailormade.tailor_app.core.model.ui.order.OrderUiModel
import kotlinx.coroutines.flow.Flow

interface OrderRepository {

  suspend fun getOrders(tailorId: String, status: String): Flow<ArrayList<OrderUiModel>>

  suspend fun acceptOrder(tailorId: String, id: String): Flow<BaseResponse>

  suspend fun rejectOrder(tailorId: String, id: String): Flow<BaseResponse>
}