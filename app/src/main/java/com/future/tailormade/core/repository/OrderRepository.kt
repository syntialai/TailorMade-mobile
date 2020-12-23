package com.future.tailormade.core.repository

import com.future.tailormade.base.model.response.BaseListResponse
import com.future.tailormade.base.model.response.BaseSingleObjectResponse
import com.future.tailormade.core.model.response.history.OrderDetailResponse
import com.future.tailormade.core.model.response.history.OrderResponse
import kotlinx.coroutines.flow.Flow

interface OrderRepository {

  suspend fun getOrders(userId: String, page: Int, itemPerPage: Int): Flow<BaseListResponse<OrderResponse>>

  suspend fun getOrderDetail(
      userId: String, id: String): Flow<BaseSingleObjectResponse<OrderDetailResponse>>
}