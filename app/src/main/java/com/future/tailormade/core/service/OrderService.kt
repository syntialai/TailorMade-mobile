package com.future.tailormade.core.service

import com.future.tailormade.base.model.response.BaseListResponse
import com.future.tailormade.base.model.response.BaseSingleObjectResponse
import com.future.tailormade.core.api.AppApiUrl
import com.future.tailormade.core.model.response.history.OrderDetailResponse
import com.future.tailormade.core.model.response.history.OrderResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface OrderService {

	@GET(AppApiUrl.USERS_ID_ORDERS_PATH)
	fun getUserOrders(@Path("userId") userId: String): BaseListResponse<OrderResponse>

	@GET(AppApiUrl.USERS_ID_ORDERS_ID_PATH)
	fun getUserOrdersByOrderId(@Path("userId") userId: String,
			@Path("id") orderId: String): BaseSingleObjectResponse<OrderDetailResponse>
}