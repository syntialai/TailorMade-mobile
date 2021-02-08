package com.future.tailormade.tailor_app.core.repository

import com.future.tailormade.base.model.response.BaseResponse
import com.future.tailormade.tailor_app.core.model.ui.order.OrderUiModel
import com.future.tailormade.tailor_app.core.model.ui.orderDetail.OrderDetailUiModel
import kotlinx.coroutines.flow.Flow

interface OrderRepository {

  suspend fun getOrders(
      tailorId: String, status: String, page: Int, itemPerPage: Int): Flow<ArrayList<OrderUiModel>>

  suspend fun getOrderDetail(tailorId: String, id: String): Flow<OrderDetailUiModel>

  suspend fun acceptOrder(tailorId: String, id: String): Flow<BaseResponse>

  suspend fun rejectOrder(tailorId: String, id: String): Flow<BaseResponse>
}